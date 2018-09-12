package com.jugg.web.connection.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ibm.db2.jcc.am.SqlInvalidAuthorizationSpecException;
import com.jugg.web.connection.middleware.rmq.producter.RmqProducterService;
import com.jugg.web.connection.mvc.db.db2.DatasourceInit;
import com.jugg.web.connection.mvc.entity.Db2Connection;
import com.jugg.web.connection.mvc.entity.vo.ErrorQueueVo;
import com.jugg.web.connection.mvc.entity.vo.ReceiveQueueVo;

@Service
public class DB2Dao {
	
	private Logger logger = LoggerFactory.getLogger(DB2Dao.class);
	
	@Autowired
	private DatasourceInit datasourceInit;
	
	@Autowired
	private RmqProducterService rmqProducterService;
	
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
	public List<Map<String, String>> getDataResults(Db2Connection db2Connection, String sql) throws SqlInvalidAuthorizationSpecException {

    	Connection connection = null ;
    	
        try {
        	connection = datasourceInit.getDdatasource(db2Connection).getConnection();
//        	connection = datasourceInit2.getDdatasource(db2Connection).getConnection();
        	
        	List<Map<String, String>> dataMaps = new ArrayList<Map<String, String>>();
        	
            try {
            	
            	sql = validationSql(sql);
                PreparedStatement ps = connection.prepareStatement(sql);
                try {
                	logger.info("Executing database query..."+sql);
                    ResultSet rs = ps.executeQuery();
                    logger.info("Processing database results...");
                    try {
                    	
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        
                        while (rs.next() == true) {
                           
                        	Map<String, String> data = Maps.newHashMap();
                        	for(int count = 1; count <= columnCount; count++) {
                        		String columName = metaData.getColumnLabel(count);
                        		data.put(columName, rs.getInt(columName)+"");
                        	}
                            dataMaps.add(data);
                        }
                    	
                    	/*Map<String, String> data = Maps.newHashMap();
                    	if(rs.next()) {
                    		data.put("count", rs.getInt(1)+"");
//                    		data.put("count", rs.getObject(1)+"");
                    		dataMaps.add(data);
                    	}*/
                        connection.commit();
                        logger.info("DB2Operation|info|colums : " + dataMaps.toString());
                        return dataMaps;
                    } catch (Exception e) {
                    	logger.error("process select sql data happen exception : ", e);
                    	throw new RuntimeException("process select sql data happen exception");
					}finally {
                        rs.close();
                    }
                }catch (Exception e) {
                	logger.error("run sql happen exception : ", e);
                	throw new RuntimeException("run sql happen exception");
				} finally {
                    ps.close();
                }
            } finally {
//                connection.close();
            	datasourceInit.closeConnection(db2Connection, connection);
            }
        } catch (SqlInvalidAuthorizationSpecException e) {
			
			logger.error("hapend db2 exception..." + e.getMessage());
//			datasourceInit.removeDatasource(db2Connection);
			throw e;
		} catch (Exception ex) {
        	try {
        		if(null != connection)
				connection.rollback();
			} catch (SQLException e) {
				logger.error("DB2Operation|error|connection rollback exception : " , e);
			}
            throw new RuntimeException(ex);
        }
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
    
    
    
    
    
    
	
}
