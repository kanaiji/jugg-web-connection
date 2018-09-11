package com.jugg.web.connection.middleware.rmq.producter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MsgSendReturnCallback implements RabbitTemplate.ReturnCallback {
	
	private Logger logger = LoggerFactory.getLogger(MsgSendReturnCallback.class);

	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		
		
		logger.error("ReturnCallback--message:"+new String(message.getBody())+",replyCode:"+replyCode+",replyText:"
                +replyText+",exchange:"+exchange+",routingKey:"+routingKey);
		
	}
	
	
} 
