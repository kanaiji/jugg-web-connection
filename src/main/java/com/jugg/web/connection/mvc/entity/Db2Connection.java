package com.jugg.web.connection.mvc.entity;

public class Db2Connection {
	
	private String _id ;
	private String database ;
	private String hostname ;
	private String port ;
	private String user_id ;
	private String password ;
	private String type ;
	
	
	public Db2Connection() {}


	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public String getDatabase() {
		return database;
	}


	public void setDatabase(String database) {
		this.database = database;
	}


	public String getHostname() {
		return hostname;
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	@Override
	public String toString() {
		return "Connection [_id=" + _id + ", database=" + database + ", hostname=" + hostname + ", port=" + port
				+ ", user_id=" + user_id + ", password=" + password + ", type=" + type + "]";
	}
	
	
	
	
	
	
	
}
