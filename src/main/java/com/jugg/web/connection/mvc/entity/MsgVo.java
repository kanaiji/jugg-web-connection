package com.jugg.web.connection.mvc.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MsgVo {
	
	private String connectionId;
	
	private String fileId;
	
	public MsgVo() {
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

	@Override
	public String toString() {
		return "MsgVo [connectionId=" + connectionId + ", fileId=" + fileId + "]";
	}
	
	
	public static void main(String[] args) {
		
		MsgVo msgVo = new MsgVo();
		msgVo.setConnectionId("111111111111111111");
		msgVo.setFileId("22222222222222222222");
		System.out.println(JSONObject.toJSONString(msgVo));
	}
	
	

}
