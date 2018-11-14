package com.jugg.web.connection.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.ibm.db2.jcc.am.SqlInvalidAuthorizationSpecException;
import com.jugg.web.connection.mvc.db.db2.DatasourceInit;
import com.jugg.web.connection.mvc.entity.Db2Connection;

@Service
public class DB2Dao {
	
	private Logger logger = LoggerFactory.getLogger(DB2Dao.class);
	
	@Autowired
	private DatasourceInit datasourceInit;
	
//	@Autowired
//	private DatasourceInit2 datasourceInit2;
	
	/**
     * This query is used to find the list of nodes that match the given criteria
     * 
     * @param pSearchString the search criteria
     * @param pMatchCount
     * @return the results the list of results
	 * @throws SqlInvalidAuthorizationSpecException 
     */
	public List<Map<String, String>> getDataResults(Db2Connection db2Connection, String sql) throws Exception {

		Connection connection = null;

		connection = datasourceInit.getDdatasource(db2Connection).getConnection();
//        	connection = datasourceInit2.getDdatasource(db2Connection).getConnection();
		List<Map<String, String>> dataMaps = new ArrayList<Map<String, String>>();

		sql = validationSql(sql);
		PreparedStatement ps = connection.prepareStatement(sql);
		logger.info("Executing database query..." + sql);
		ResultSet rs = ps.executeQuery();
		logger.info("Processing database results...");

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		while (rs.next() == true) {

			Map<String, String> data = Maps.newHashMap();
			for (int count = 1; count <= columnCount; count++) {
				String columName = metaData.getColumnLabel(count);
				data.put(columName, rs.getInt(columName) + "");
			}
			dataMaps.add(data);
		}

		/*
		 * Map<String, String> data = Maps.newHashMap(); if(rs.next()) {
		 * data.put("count", rs.getInt(1)+""); // data.put("count", rs.getObject(1)+"");
		 * dataMaps.add(data); }
		 */
		connection.commit();
		logger.info("DB2Operation|info|colums : " + dataMaps.toString());
		rs.close();
		ps.close();
//                connection.close();
		datasourceInit.closeConnection(db2Connection, connection);
		return dataMaps;
	}

	
	//效验sql
	private static String validationSql(String sql) {
		
		int lenth = sql.lastIndexOf(";");
		String str = sql.substring(0, lenth);
		return str;
	}

		
    public static void main(String[] args) {
    	
    	String sql = "SELECT sum(AMOUNT) FROM SCTID.IBM_FINANCIALS WHERE FINANCIALS_SOURCE in ('WWSIGN','WWSD') AND BRANCH in (176,248,0674,0073,0672,0247,0676,0180,0678,0017,0799) AND DELETED=0 AND QUARTER=1 AND REVENUE_TYPE='Transactional' AND YEAR=2018;";
    	
    	sql=validationSql(sql);
    	
    	System.out.println(sql);
	}
    
    
    public void testConnection(Db2Connection db2Connection) throws Exception{
    	
		DruidDataSource dataSource = datasourceInit.testConnection(db2Connection);
		Connection connection = dataSource.getConnection() ;
		Statement statement = connection.createStatement();
//		String sql = "SELECT CREATOR, TYPE, NAME, REMARKS FROM SYSIBM.SYSTABLES WHERE TYPE = 'T' AND CREATOR = ' "+ db2Connection.getUser_id() + "';";
//		PreparedStatement ps = connection.prepareStatement(sql);
//     	logger.info("Executing database query...sql :"+ sql);
//        ResultSet rs = statement.executeQuery();
//        rs.close();
		statement.close();
        connection.close();
		dataSource.close();
		//移除GCRoot 引用
		datasourceInit.removeDatasource(db2Connection);
    }
    
    
    
	
}
