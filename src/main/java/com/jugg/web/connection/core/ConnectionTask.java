package com.jugg.web.connection.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jugg.web.connection.init.Init;
import com.jugg.web.connection.mvc.entity.MsgVo;


/**
 * <b>功能说明:跟新地图任务 TODO... 无注释
 */
public class ConnectionTask {

	private static Logger logger = LoggerFactory.getLogger(ConnectionTask.class);


	private ConnectionPersist connectionPersist = Init.connectionPersist;

	private MsgVo msgVo ;
	
	public ConnectionTask(MsgVo msgVo) {
		this.msgVo = msgVo;
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

		} catch (Exception e) {

			if (executeCount > executeMaxCount) {
				// 失败3次后，db mapStatus 为 fail , executeCount 重置为0，处理下一条task.
				executeCount = 0;
				logger.warn("connection error for executeCount is max count... need send error msg to rabbitmq error_queue.");
				//send error msg to error queue..again recive
				connectionPersist.sendError(msgVo);
				return;
			}
			logger.error("connection error, executeCount={}, error stack info: {}", executeCount, e); 
			executeCount++;
			return;

		}

	}

}
