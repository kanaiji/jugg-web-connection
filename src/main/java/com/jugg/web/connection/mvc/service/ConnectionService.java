package com.jugg.web.connection.mvc.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jugg.web.connection.middleware.rmq.producter.RmqProducterService;
import com.jugg.web.connection.mvc.dao.DB2Dao;
import com.jugg.web.connection.mvc.dao.MongoDao;
import com.jugg.web.connection.mvc.entity.Db2Connection;
import com.jugg.web.connection.mvc.entity.JuggFile;

//import com.jugg.web.connection.db.db2.DB2Operation;

/**
 * connection 
 * @author Tony
 *
 */
@Service
public class ConnectionService {

	
	private Logger logger = LoggerFactory.getLogger(ConnectionService.class);
	
	@Autowired
    private MongoDao mongoDbDao;
	
	@Autowired
    private DB2Dao dB2Dao;
	
	@Autowired
	private RmqProducterService rmqProducterService;
	
	
	public void runSql(String conId, String fileId) {
		
		Db2Connection db2Connection= mongoDbDao.findConnectionById(conId);
		if(null == db2Connection) {
			logger.warn("connectionService runSql method : the connectionId no mapping mongo document..so return");
			return;
		}
		logger.info("connection document info: "+db2Connection.toString());
		
		JuggFile juggFile = mongoDbDao.findJuggFileById(fileId);
		if(null == juggFile) {
			logger.warn("connectionService runSql method : the fileId no mapping mongo document..so return");
			return;
		}
		logger.info("file document info: "+juggFile.toString());
		
		List<Map<String, String>> datas = dB2Dao.getDataResults(db2Connection, juggFile.getFile_content());
		
		String result = JSONObject.toJSONString(datas);
		rmqProducterService.sendResult(result);
		
		logger.info("run sql result is :" + result);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
