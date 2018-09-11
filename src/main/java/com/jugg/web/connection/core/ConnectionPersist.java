package com.jugg.web.connection.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jugg.web.connection.middleware.rmq.producter.RmqProducterService;
import com.jugg.web.connection.mvc.entity.MsgVo;
import com.jugg.web.connection.mvc.service.ConnectionService;


/**
 * <b>功能说明:
 * TODO... 无注释
 * 
 */
@Service("connectionPersist")
public class ConnectionPersist {

	private static Logger logger = LoggerFactory.getLogger(ConnectionPersist.class);

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
	public void runSql(MsgVo msgVo) throws Exception{
		
		connectionService.runSql(msgVo.getConnectionId(), msgVo.getFileId());
		
	}
	
	
	//send error msg to queue
	public void sendError(MsgVo msgVo) {
		
		String msg = JSONObject.toJSONString(msgVo);
		rmqProducterService.sendError(msg);
		
	}
	
	


}
