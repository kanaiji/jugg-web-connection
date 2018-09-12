package com.jugg.web.connection.mvc.db.db2;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.jugg.web.connection.mvc.entity.Db2Connection;
import com.jugg.web.connection.utils.MD5Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("datasourceInit")
public class DatasourceInit {

	private static Logger logger = LoggerFactory.getLogger(DatasourceInit.class);
	
    public static final Map<String, DruidDataSource> datasourceMap = Maps.newHashMap();
    
    public static final Map<String, String> checkMap = Maps.newHashMap();

	public DruidDataSource initialize(Db2Connection db2Connection) {

        String db2name = db2Connection.getDatabase();
        String host = db2Connection.getHostname();
        String port = db2Connection.getPort();
        String user = db2Connection.getUser_id();
        String pass = db2Connection.getPassword();
        
        String url = "jdbc:db2://" + host + ":"+ port + "/"+ db2name ; //+ "?characterEncoding=utf8" ;
        
        logger.info("Creating datasource for url=" + url + "... user=" + user);

		DruidDataSource datasource = new DruidDataSource();
		
		datasource.setUrl(url);
		datasource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
		datasource.setUsername(user);
		datasource.setPassword(pass);
		datasource.setInitialSize(1);
		datasource.setMinIdle(3);
		datasource.setMaxWait(120000);
		//max connection total
		datasource.setMaxActive(3);
		datasource.setMinEvictableIdleTimeMillis(300000);
//        datasource.setTimeBetweenEvictionRunsMillis(60000);
//        datasource.setTestWhileIdle(true);
//        datasource.setTestOnBorrow(true);
//        datasource.setTestOnReturn(false);
//        datasource.setPoolPreparedStatements(true);
        
        String key = host + ":" + port;
        datasourceMap.put(key, datasource);
        
        return datasource;
        
    }

  
    // get db pool
    public DruidDataSource getDdatasource(Db2Connection db2Connection) {
    	
    	 String host = db2Connection.getHostname();
         String port = db2Connection.getPort();
         String key = host + ":" + port;
         String checkKey = MD5Util.encode(db2Connection.toString());
         
         // 新增
         if(null == datasourceMap.get(key)) {
        	 checkMap.put(key, checkKey);
        	 return initialize(db2Connection);
         }
         
         if(null != checkMap.get(key) && !checkMap.get(key).equals(checkKey) ) {
        	 //重新插入新的值
        	 checkMap.put(key, checkKey);
        	 return initialize(db2Connection);
         }
         
    	 return datasourceMap.get(key);
    	
    }
    
    
    public void removeDatasource(Db2Connection db2Connection) {
    	String host = db2Connection.getHostname();
        String port = db2Connection.getPort();
        String key = host + ":" + port;
        datasourceMap.remove(key);
    }
    
    
    
    // 回收connetion 对象
    public boolean closeConnection(Db2Connection db2Connection, Connection connettion) {
    	
    	String host = db2Connection.getHostname();
        String port = db2Connection.getPort();
        
        String key = host + ":" + port;
        DruidDataSource dataSource = datasourceMap.get(key);
        if(null == dataSource) {
        	logger.info("close connection , but the connection's pool is null, so close the connection..");
        	try {
				connettion.close();
			} catch (SQLException e) {
				logger.error("close connection find error..", e);
				return false;
			}
        	return true;
        }
        
        // connection to pool
        dataSource.discardConnection(connettion);
        
        logger.info("the connection back the pool successful");
        
        return true ;
    	
    }
    
    
    
    

}
