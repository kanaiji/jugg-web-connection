package com.jugg.web.connection.mvc.entity.vo;

import com.alibaba.fastjson.JSONObject;

public class ReceiveQueueVo {
	
	private String connectionId;
	
	private String fileId;
	
	private String logId;
	
	private String type;
	
	private String job_id;
	
	
	public ReceiveQueueVo() {
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	@Override
	public String toString() {
		return "ReceiveQueueVo [connectionId=" + connectionId + ", fileId=" + fileId + ", logId=" + logId + ", type="
				+ type + ", job_id=" + job_id + "]";
	}
	
	
	public static void main(String[] args) {
		
		ReceiveQueueVo msgVo = new ReceiveQueueVo();
		msgVo.setConnectionId("111111111111111111");
		msgVo.setFileId("22222222222222222222");
		System.out.println(JSONObject.toJSONString(msgVo));
	}
	
	

}
