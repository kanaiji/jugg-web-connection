package com.jugg.web.connection.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jugg.web.connection.middleware.rmq.producter.RmqProducterService;
import com.jugg.web.connection.mvc.entity.ApiCommonResultVo;
import com.jugg.web.connection.mvc.entity.Db2Connection;
import com.jugg.web.connection.mvc.entity.vo.ReceiveQueueVo;
import com.jugg.web.connection.mvc.service.ConnectionService;


/**
 * 
 * @author Tony
 *
 */
//@RestController
@Controller
@RequestMapping("/jugg/connection/")
public class JuggConnectionController {

	
	private Logger log = LoggerFactory.getLogger(JuggConnectionController.class);
	
	
	@Autowired
	private RmqProducterService rmqProducterService;
	
	@Autowired
	private ConnectionService connectionService;
	
	
	
	/**
	 * test db2 conneciton info
	 * @param ReceiveQueueVo 
	 * @return
	 */
	@RequestMapping("testConnection")
	@ResponseBody
	public ApiCommonResultVo testConnection(Db2Connection db2Connection) {
		
		log.info("JuggConnectionController|testConnection() | params = " + db2Connection.toString());
		String db2name = db2Connection.getDatabase();
        String host = db2Connection.getHostname();
        String port = db2Connection.getPort();
        String user = db2Connection.getUser_id();
        String pass = db2Connection.getPassword();
		
        if(StringUtils.isEmpty(db2name)) {
        	log.info("JuggConnectionController|testConnection() | db2name is null");
        	return new ApiCommonResultVo(-1, "db2name is null", "");
        }
        if(StringUtils.isEmpty(host)) {
        	log.info("JuggConnectionController|testConnection() | host is null");
        	return new ApiCommonResultVo(-1, "host is null", "");
        }
        if(StringUtils.isEmpty(port)) {
        	log.info("JuggConnectionController|testConnection() | port is null");
        	return new ApiCommonResultVo(-1, "port is null", "");
        }
        if(StringUtils.isEmpty(user)) {
        	log.info("JuggConnectionController|testConnection() | user is null");
        	return new ApiCommonResultVo(-1, "user is null", "");
        }
        if(StringUtils.isEmpty(pass)) {
        	log.info("JuggConnectionController|testConnection() | pass is null");
        	return new ApiCommonResultVo(-1, "pass is null", "");
        }
        
		// 调用service
		return connectionService.testConnection(db2Connection);

	}
	
	
	
	
	
	

	/**
	 * @return
	 */
	@RequestMapping("test")
	@ResponseBody
	public String test() {
		try {
			Thread.sleep(3000L);
			JSONObject json = new JSONObject();
			json.put("job_id", "4432");
			json.put("type", "SQL");
			json.put("connectionId", "5b851eab39c4143c11479ead");
			json.put("fileId", "5b8520aa39c4143c11479eae");
			json.put("logId", 1131);
			
			System.out.println(json.toJSONString() + " lenth :" + json.toJSONString().length());
//			rmqProducterService.sendResult(json.toJSONString());
			
			rmqProducterService.sendReceive(json.toJSONString());
			
			return "大耍个";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail-丑逼";
		}

	}
	
	
	/**
	 * @param orderId
	 * @return
	 */
	@RequestMapping("testPage")
	public String testPage() {
		try {
//			Thread.sleep(3000L);
			// 调用service
			ReceiveQueueVo receiveQueueVo = new ReceiveQueueVo();
			receiveQueueVo.setConnectionId("5b851eab39c4143c11479ead");
			receiveQueueVo.setFileId("5b8520aa39c4143c11479eae");
			String msg = JSONObject.toJSONString(receiveQueueVo);
			rmqProducterService.sendReceive(msg);
			return "login";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "login";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
