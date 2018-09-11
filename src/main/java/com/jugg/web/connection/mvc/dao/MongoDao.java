package com.jugg.web.connection.mvc.dao;

import org.springframework.stereotype.Service;

import com.jugg.web.connection.common.CommonConts;
import com.jugg.web.connection.mvc.db.mongo.MongoDBTemplet;
import com.jugg.web.connection.mvc.entity.Db2Connection;
import com.jugg.web.connection.mvc.entity.JuggFile;

@Service
public class MongoDao{
	
    private MongoDBTemplet mongoDBTemplet = MongoDBTemplet.getInstance(CommonConts.MONGO_DB_IP, CommonConts.MONGO_DB_PORT);
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Db2Connection findConnectionById(String _id) {
		
		return mongoDBTemplet.findById(CommonConts.MONGO_DB_NAME, CommonConts.MONGO_CONNECTION_NAME_CONNETION, _id, Db2Connection.class);
	    
    }

	/**
	 * 
	 * @param id
	 * @return
	 */
	public JuggFile findJuggFileById(String _id) {
		
		return mongoDBTemplet.findById(CommonConts.MONGO_DB_NAME, CommonConts.MONGO_CONNECTION_NAME_FILE, _id, JuggFile.class);
	    
    }
	
   
	
	
	
	

}
