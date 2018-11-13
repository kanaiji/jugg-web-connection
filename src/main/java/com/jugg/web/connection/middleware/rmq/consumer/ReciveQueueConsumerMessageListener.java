package com.jugg.web.connection.middleware.rmq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jugg.web.connection.core.ConnectionTask;
import com.jugg.web.connection.init.Init;
import com.jugg.web.connection.mvc.entity.vo.ReceiveQueueVo;
import com.rabbitmq.client.Channel;

@Component
public class ReciveQueueConsumerMessageListener implements ChannelAwareMessageListener{
	
	private Logger logger = LoggerFactory.getLogger(ReciveQueueConsumerMessageListener.class);

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// run
		try {
			// one time consumer one..
			channel.basicQos(1);
			byte[] body = message.getBody();
			String jsonStr =  new String(body, "UTF-8");
			// fastjson
	        ReceiveQueueVo msgVo = JSONObject.parseObject(jsonStr, ReceiveQueueVo.class);
			
			// add message to java local queue..
			Init.tasks.put(new ConnectionTask(msgVo));
			
            MessageProperties messageProperties = message.getMessageProperties();
            
			// 显示调用channel.basicAck(envelope.getDeliveryTag(), false);来告诉消息服务器来删除消息
            
            channel.basicAck(messageProperties.getDeliveryTag(), false); 
            
            logger.info("consumer recive queue message success.. : " + msgVo.toString());
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
