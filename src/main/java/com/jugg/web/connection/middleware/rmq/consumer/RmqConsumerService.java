package com.jugg.web.connection.middleware.rmq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jugg.web.connection.core.ConnectionTask;
import com.jugg.web.connection.init.Init;
import com.jugg.web.connection.mvc.entity.MsgVo;
import com.rabbitmq.client.Channel;

@Component
public class RmqConsumerService {
	
	private Logger logger = LoggerFactory.getLogger(RmqConsumerService.class);
	
	@RabbitListener(queues = "connection-receive-queue")
	public void messageConsumer(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
			throws Exception {
		// run
		try {
			// one time consumer one..
			channel.basicQos(1);
			byte[] body = message.getBody();
			String jsonStr =  new String(body, "UTF-8");
			// fastjson
	        MsgVo msgVo = JSONObject.parseObject(jsonStr, MsgVo.class);
			logger.warn("RmqConsumerService message : " + msgVo.toString());
			
			// add message to java local queue..
			Init.tasks.put(new ConnectionTask(msgVo));
			
			// ack - notify MQ，delete the message..
			channel.basicAck(tag, false);
		} catch (Exception e) {
			
			// requeue：true again add queue，false：discard the message，delete it .
			if(message.getMessageProperties().getRedelivered()){
				channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);  
				logger.error("app-push|consumer|bad|DeliveryTag:{}|the message is fail 2 times, so we need delete the message, because it is bad message..{}",message.getMessageProperties().getDeliveryTag(), e);
			}else{
				channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);  
				//happend error..: run notify Mq channel.basicReject() 'Refuse to reply'，again the message add queue...
				logger.error("app-push|consumer|again|DeliveryTag:{}|find exception, the message return map-queue, wjDataLog id: {}", message.getMessageProperties().getDeliveryTag(), e);
			}
			
		}

	}

}
