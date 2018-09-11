package com.jugg.web.connection.mvc.db.db2;

import com.google.common.collect.Maps;
import com.ibm.db2.jcc.DB2DataSource;
import com.jugg.web.connection.mvc.entity.Db2Connection;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@SuppressWarnings("deprecation")
@Service("datasourceInit2")
public class DatasourceInit2 {

	private static Logger logger = LoggerFactory.getLogger(DatasourceInit2.class);
	
    public static final Map<String, DB2DataSource> datasourceMap = Maps.newHashMap();

	public DB2DataSource initialize(Db2Connection db2Connection) {

        System.setProperty("java.awt.headless", "true");

        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String db2name = db2Connection.getDatabase();
        String host = db2Connection.getHostname();
        String port = db2Connection.getPort();
        String user = db2Connection.getUser_id();
        String pass = db2Connection.getPassword();
        
        String url = "jdbc:db2://" + host + ":"+ port + "/"+ db2name + "?characterEncoding=utf8" ;
        
        logger.info("Creating datasource for url=" + url + "... user=" + user);

        DB2DataSource dataSource = new DB2DataSource();
        dataSource.setDatabaseName(db2name);
        dataSource.setPortNumber(Integer.valueOf(port));
        dataSource.setServerName(host);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        dataSource.setDriverType(4);
        

        String key = host + ":" + port;
        datasourceMap.put(key, dataSource);
        
        return dataSource;
        
    }

  
    public DB2DataSource getDdatasource(Db2Connection db2Connection) {
    	
    	String host = db2Connection.getHostname();
        String port = db2Connection.getPort();
         
         String key = host + ":" + port;
         if(null == datasourceMap.get(key)) {
        	 return initialize(db2Connection);
         }
         
    	 return datasourceMap.get(key);
    	
    }
    
    
    
    
    
    
    

}
