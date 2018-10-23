package com.jugg.web.connection.middleware.rmq.producter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
 
public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {
	
	
	private Logger logger = LoggerFactory.getLogger(MsgSendReturnCallback.class);
	
	
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
        	logger.info("消息确认成功cause:"+cause + " ack:" + ack);
        } else {
        	//处理丢失的消息
        	logger.error("消息确认失败:"+correlationData.getId()+"#cause:"+cause);
        }
    }
    
    
    
}
