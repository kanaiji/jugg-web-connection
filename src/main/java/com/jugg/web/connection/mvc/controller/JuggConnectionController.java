package com.jugg.web.connection.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jugg.web.connection.middleware.rmq.producter.RmqProducterService;
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

	@Autowired
	private RmqProducterService rmqProducterService;
	
	@Autowired
	private ConnectionService connectionService;
	
	

	/**
	 * @return
	 */
	@RequestMapping("test")
	@ResponseBody
	public String test(String orderId) {
		try {
//			Thread.sleep(3000L);
			JSONObject json = new JSONObject();
			json.put("username", "admin");
			json.put("password", "123qwe");
			json.put("host", "9.42.89.202");
			json.put("port", "5672");
			rmqProducterService.sendResult(json.toJSONString());
			
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
			
			rmqProducterService.sendResult("jugg  connection producter test ..");
			rmqProducterService.sendError("jugg  connection producter test ..");
			return "login";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "login";
	}
	
	
	
	/**
	 * test mongodb
	 * @param orderId
	 * @return
	 */
	@RequestMapping("mgoTest")
	@ResponseBody
	public String mgoTest(String id) {
		try {
//			Thread.sleep(3000L);
			// 调用service
			
			connectionService.runSql(id, "5b8520aa39c4143c11479eae");
			
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
