package com.jugg.web.connection.mvc.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.db2.jcc.am.DisconnectNonTransientConnectionException;
import com.ibm.db2.jcc.am.SqlInvalidAuthorizationSpecException;
import com.ibm.db2.jcc.am.SqlNonTransientConnectionException;
import com.ibm.db2.jcc.am.SqlSyntaxErrorException;
import com.jugg.web.connection.mvc.dao.DB2Dao;
import com.jugg.web.connection.mvc.dao.MongoDao;
import com.jugg.web.connection.mvc.entity.ApiCommonResultVo;
import com.jugg.web.connection.mvc.entity.Db2Connection;
import com.jugg.web.connection.mvc.entity.JuggFile;

//import com.jugg.web.connection.db.db2.DB2Operation;

/**
 * connection
 * 
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

	public List<Map<String, String>> runSql(String conId, String fileId) throws Exception {

		Db2Connection db2Connection = mongoDbDao.findConnectionById(conId);
		if (null == db2Connection) {
			logger.warn("connectionService runSql method : the connectionId no mapping mongo document..so return");
			return null;
		}
		logger.info("connection document info: " + db2Connection.toString());

		JuggFile juggFile = mongoDbDao.findJuggFileById(fileId);
		if (null == juggFile) {
			logger.warn("connectionService runSql method : the fileId no mapping mongo document..so return");
			return null;
		}
		logger.info("file document info: " + juggFile.toString());

		List<Map<String, String>> datas = dB2Dao.getDataResults(db2Connection, juggFile.getFile_content());
		return datas;

	}

	public ApiCommonResultVo testConnection(Db2Connection db2Connection){

		try {
			dB2Dao.testConnection(db2Connection);
			return new ApiCommonResultVo(0, "success", "");
		} catch (SqlNonTransientConnectionException e) {
			logger.error("DB2Dao|testConnection() happend error ....", e);
			return new ApiCommonResultVo(-2, "unknow hostname", e.getMessage());
		} catch (SqlInvalidAuthorizationSpecException e) {
			logger.error("DB2Dao|testConnection() happend error ....", e);
			return new ApiCommonResultVo(-2, "user or password is incorrect", e.getMessage());
		} catch (SqlSyntaxErrorException e) {
			logger.error("DB2Dao|testConnection() happend error ....", e);
			return new ApiCommonResultVo(-2, "db2 url invalid or no auth", e.getMessage());
		} catch (DisconnectNonTransientConnectionException e) {
			logger.error("DB2Dao|testConnection() happend error ....", e);
			return new ApiCommonResultVo(-3, "unknow port or database", e.getMessage());
		} catch (Exception e) {
			logger.error("DB2Dao|testConnection() happend error ....", e);
			return new ApiCommonResultVo(-3, "system exception, please call admin", e.getMessage());
		}
		
	}
	
}
