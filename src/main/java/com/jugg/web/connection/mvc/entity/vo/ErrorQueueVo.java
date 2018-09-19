package com.jugg.web.connection.mvc.entity.vo;

/**
 * 封装error 信息的 实体类
 * @author JingWangZou
 *
 */
public class ErrorQueueVo {
	
	 private String code ;
     
     private String message;
     
     private ReceiveQueueVo receiveQueueVo;
     
     public ErrorQueueVo() {
	 }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReceiveQueueVo getReceiveQueueVo() {
		return receiveQueueVo;
	}

	public void setReceiveQueueVo(ReceiveQueueVo receiveQueueVo) {
		this.receiveQueueVo = receiveQueueVo;
	}

	@Override
	public String toString() {
		return "ErrorQueueVo [code=" + code + ", message=" + message + ", receiveQueueVo=" + receiveQueueVo + "]";
	}
     
     
     
	
	
	/**
	 * 从db2 最上层异常堆栈信息中截取 db2 错误code
	 * @param mesage
	 * @return
	 */
	public String getDb2ErrorCode(String mesage) {
		int num = mesage.indexOf("ERRORCODE=");
		int endnum = mesage.indexOf(", SQLSTATE");
		String str = mesage.substring(num, endnum);
		String[] strArray = str.split("=");
		String result = strArray[1];
		return result;
	}
	
	
	
	
	public static void main(String[] args) {
		
		String aa = "[jcc][t4][2013][11249][3.69.24] 发生了连接权限故障。原因：用户标识或密码无效。 ERRORCODE=-4214, SQLSTATE=28000";
		
		int num = aa.indexOf("ERRORCODE=");

		int endnum = aa.indexOf(", SQLSTATE");
		
		String bb = aa.substring(num, endnum);
		
		String[] strArray = bb.split("=");
		
        System.out.println(strArray[1]);
		
	}
	
	
	
	
	
	
    
     
     

}