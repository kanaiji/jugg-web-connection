package com.jugg.web.connection.core;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jugg.web.connection.middleware.rmq.producter.RmqProducterService;
import com.jugg.web.connection.mvc.entity.vo.ErrorQueueVo;
import com.jugg.web.connection.mvc.entity.vo.ReceiveQueueVo;
import com.jugg.web.connection.mvc.service.ConnectionService;


/**
 * <b>功能说明:
 * TODO... 无注释
 * 
 */
@Service("connectionPersist")
public class ConnectionPersist {

	private static Logger logger = LoggerFactory.getLogger(ConnectionPersist.class);

	private static final String receive_vo_key = "receive_vo";
	
	private static final String result_key = "result";
	
	private static final String error_key = "error";
	
    @Autowired
    private ConnectionService connectionService;

    @Autowired
	private RmqProducterService rmqProducterService;

    /**
     * 获取数据
     * @param dataId
     * @param dataType
     * @param hospitalId
     */
	public void runSql(ReceiveQueueVo msgVo) throws Exception{
		
		List<Map<String, String>> datas = connectionService.runSql(msgVo.getConnectionId(), msgVo.getFileId());
		
		JSONObject json = new JSONObject();
		json.put(result_key, datas !=null ? datas : "");
		json.put(receive_vo_key, msgVo);
		String str = json.toString();
		rmqProducterService.sendResult(str);
		
		logger.info("run sql result is :" + str);
		
	}
	
	
	//send error msg to queue
	public void sendError(String message, String details, ReceiveQueueVo receiveQueueVo) {
		
		ErrorQueueVo errorQueueVo = new ErrorQueueVo();
		errorQueueVo.setMessage(message);
		errorQueueVo.setDetails(details);
		errorQueueVo.setCode(errorQueueVo.getDb2ErrorCode(message));
		
		JSONObject json = new JSONObject();
		json.put(error_key, errorQueueVo);
		json.put(receive_vo_key, receiveQueueVo);
		String str = json.toJSONString();
		
		try {
			rmqProducterService.sendError(str);
		} catch (AmqpException | UnsupportedEncodingException e) {
			logger.error("ConnectionPersist.sendError()| the queue msg is String type, happend 'AmqpException | UnsupportedEncodingException' when String get byte[] and set Utf-8.. ");
		}
		
	}
	
	


}
