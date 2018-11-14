package com.jugg.web.connection.core;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.db2.jcc.am.DisconnectNonTransientConnectionException;
import com.ibm.db2.jcc.am.SqlInvalidAuthorizationSpecException;
import com.ibm.db2.jcc.am.SqlNonTransientConnectionException;
import com.ibm.db2.jcc.am.SqlSyntaxErrorException;
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
	 * @throws IOException 
	 */
	public void run() throws IOException {

		// 去跟新
		try {

			/** 执行sql **/
			connectionPersist.runSql(msgVo);
			// 重置为0，开始下一个task的处理
			channel.basicAck(tag, false); 
			logger.info("consumer recive queue message success.. basicAck delete this message");
			
		} catch (SqlInvalidAuthorizationSpecException e) {
			logger.error("hapend db2 exception..." , e);
			channel.basicAck(tag, false);
			connectionPersist.sendError("user or password is incorrect", e.getMessage(), CommonConts.DB2_ERROR_CODE_AUTH, msgVo);
			return;
		} catch (SqlNonTransientConnectionException e) {
			logger.error("hapend db2 exception..." , e);
			channel.basicAck(tag, false);
			connectionPersist.sendError("unknow hostname", e.getMessage(), CommonConts.DB2_ERROR_CODE_CONNECTION, msgVo);
			return;
		} catch (DisconnectNonTransientConnectionException e) {
			logger.error("hapend db2 exception..." , e);
			channel.basicAck(tag, false);
			connectionPersist.sendError("unknow port or database", e.getMessage(), CommonConts.DB2_ERROR_CODE_HOSTNAME, msgVo);
			return;
		} catch (SqlSyntaxErrorException e) {
			logger.error("hapend db2 exception..." , e);
			channel.basicAck(tag, false);
			connectionPersist.sendError("db2 url invalid", e.getMessage(), CommonConts.DB2_ERROR_CODE_URL, msgVo);
			return;
		} catch (Exception e) {
			logger.warn("connection error for executeCount is max count... need send error msg to rabbitmq error_queue.");
			channel.basicAck(tag, false);
			//send error msg to error queue..again recive
			connectionPersist.sendError("run max again, still hapend error", "unknow..look up log file ", CommonConts.DB2_ERROR_CODE_UNKNOW, msgVo);
			return;

		}

	}

}
