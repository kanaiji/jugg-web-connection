package com.jugg.web.connection.core;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.db2.jcc.am.DisconnectNonTransientConnectionException;
import com.ibm.db2.jcc.am.SqlInvalidAuthorizationSpecException;
import com.ibm.db2.jcc.am.SqlNonTransientConnectionException;
import com.jugg.web.connection.common.CommonConts;
import com.jugg.web.connection.init.Init;
import com.jugg.web.connection.mvc.entity.vo.ReceiveQueueVo;
import com.rabbitmq.client.Channel;


/**
 * <b>功能说明:跟新地图任务 TODO... 无注释
 */
public class ConnectionTask {

	private static Logger logger = LoggerFactory.getLogger(ConnectionTask.class);


	private ConnectionPersist connectionPersist = Init.connectionPersist;

	private ReceiveQueueVo msgVo ;
	
	private Channel channel;
	
	private long tag;
	
	public ConnectionTask(ReceiveQueueVo msgVo, Channel channel, long tag) {
		this.msgVo = msgVo;
		this.channel = channel;
		this.tag = tag;
	}
	

	/**
	 * 执行更新map.
	 */
	public void run(int executeCount, int executeMaxCount) {

		// 去跟新
		try {

			/** 执行sql **/
			connectionPersist.runSql(msgVo);
			// 重置为0，开始下一个task的处理
			executeCount = 0;
			channel.basicAck(tag, false); 
			logger.info("consumer recive queue message success.. basicAck delete this message");
			
		} catch (SqlInvalidAuthorizationSpecException e) {
			logger.error("hapend db2 exception..." , e);
			try {
				channel.basicAck(tag, false);
			} catch (IOException e1) {
			} 
			connectionPersist.sendError("user or password is incorrect", e.getMessage(), CommonConts.DB2_ERROR_CODE_AUTH, msgVo);
			executeCount = 0;
			return;
		} catch (SqlNonTransientConnectionException e) {
			logger.error("hapend db2 exception..." , e);
			try {
				channel.basicAck(tag, false);
			} catch (IOException e1) {
			} 
			connectionPersist.sendError("unknow hostname", e.getMessage(), CommonConts.DB2_ERROR_CODE_CONNECTION, msgVo);
			executeCount = 0;
			return;
		} catch (DisconnectNonTransientConnectionException e) {
			logger.error("hapend db2 exception..." , e);
			try {
				channel.basicAck(tag, false);
			} catch (IOException e1) {
			} 
			connectionPersist.sendError("unknow port or database", e.getMessage(), CommonConts.DB2_ERROR_CODE_HOSTNAME, msgVo);
			executeCount = 0;
			return;
		} catch (Exception e) {
			if (executeCount > executeMaxCount) {
				// 失败3次后，db mapStatus 为 fail , executeCount 重置为0，处理下一条task.
				executeCount = 0;
				logger.warn("connection error for executeCount is max count... need send error msg to rabbitmq error_queue.");
				try {
					channel.basicAck(tag, false);
				} catch (IOException e1) {
				} 
				//send error msg to error queue..again recive
				connectionPersist.sendError("run max again, still hapend error", "unknow..look up log file ", CommonConts.DB2_ERROR_CODE_UNKNOW, msgVo);
				return;
			}
			logger.error("connection error, executeCount={}, error stack info: {}", executeCount, e); 
			executeCount++;
			return;

		}

	}

}
