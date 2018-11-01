package com.jugg.web.connection.middleware.rmq.producter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jugg.web.connection.middleware.rmq.config.RabbitConfig;

@Service
public class RmqProducterService {
	
	private Logger logger = LoggerFactory.getLogger(RmqProducterService.class);
	
	@Autowired
    private AmqpTemplate rabbitTemplate;

	
    public void sendReceive(String msg) {
		
		logger.info("RMQ-PRODUCTER|info|sendReceive|msg {}", msg);
        rabbitTemplate.send(RabbitConfig.connectionReceiveExchangeName, RabbitConfig.connectionReceiveRouteName, new Message(msg.getBytes(),new MessageProperties()));
        
    }
	
	public void sendResult(String msg) {
		
		logger.info("RMQ-PRODUCTER|info|sendResult|msg {}", msg);
        rabbitTemplate.send(RabbitConfig.connectionResultExchangeName, RabbitConfig.connectionResultRouteName, new Message(msg.getBytes(),new MessageProperties()));
        
    }
	
    public void sendError(String msg) throws AmqpException, UnsupportedEncodingException {
		
		logger.info("RMQ-PRODUCTER|info|sendError|msg {}", msg);
        rabbitTemplate.send(RabbitConfig.runErrorExchangeName, RabbitConfig.runErrorRouteName, new Message(msg.getBytes("utf-8"),new MessageProperties()));
        
    }


}
