package com.jugg.web.connection.mvc.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jugg.web.connection.middleware.rmq.producter.RmqProducterService;
import com.jugg.web.connection.mvc.entity.ApiCommonResultVo;
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
	 * api 执行 connection 逻辑
	 * @param ReceiveQueueVo 提供api 接口
	 * @return
	 */
	@RequestMapping("run")
	@ResponseBody
	public ApiCommonResultVo run(ReceiveQueueVo receiveQueueVo) {
		try {
			// 调用service
			List<Map<String, String>> datas = connectionService.runSql(receiveQueueVo.getConnectionId(), receiveQueueVo.getFileId());
			
			return new ApiCommonResultVo(0, "success", datas);
		} catch (Exception e) {
			log.error("JuggConnectionController|run() happend error ....", e);
			return new ApiCommonResultVo(-1, "发生异常", "");
		}

	}
	
	
	
	
	
	

	/**
	 * @return
	 */
	@RequestMapping("test")
	@ResponseBody
	public String test(String orderId) {
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
			
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
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
