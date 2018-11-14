package com.jugg.web.connection.common;

/**
 * 常量类
 * @author JingWangZou
 *
 */
public class CommonConts {
	
	
	/** db2 config final  **/
	public static final String DB2_NAME = "db2Name";
    
	public static final String DB2_HOST = "db2Host";
    
	public static final String DB2_PORT = "db2Port";
    
	public static final String DB2_USER = "db2User";
    
	public static final String DB2_PASS = "db2Pass";
	
	
	/** mongodb dbname ... info  **/
	public static final String MONGO_DB_IP = "9.42.89.202";
	
	public static final int MONGO_DB_PORT = 27017;
	
	public static final String MONGO_DB_NAME = "jugg"; //Connection
	
	public static final String MONGO_CONNECTION_NAME_CONNETION = "connection";
	
	public static final String MONGO_CONNECTION_NAME_FILE = "file";
	
	public static final String MONGO_CONNECTION_NAME_USERS = "users";
	
	
	
	/** rabbit mq config info . **/
	public static final int DB2_ERROR_CODE_UNKNOW = 400;  // unknow excepiton
	
	public static final int DB2_ERROR_CODE_HOSTNAME = 401;  // unknow hostname
	
	public static final int DB2_ERROR_CODE_AUTH = 402;  // user or password is incorrect
	
	public static final int DB2_ERROR_CODE_CONNECTION = 403;   // unknow port or database
	
	public static final int DB2_ERROR_CODE_URL = 404;   // db2 url invalid
	
}
