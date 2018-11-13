package com.jugg.web.connection.middleware.rmq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.jugg.web.connection.middleware.rmq.consumer.ReciveQueueConsumerMessageListener;

@Configuration
public class RabbitConsumerConfig {

	
	//run_error_queue
	//connection_receive_queue
	@Bean
	public SimpleMessageListenerContainer messageContainer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory,
			@Qualifier("connection_receive_queue") Queue queue) {
		/*Queue[] q = new Queue[queues.split(",").length];
		for (int i = 0; i < queues.split(",").length; i++) {
			q[i] = new Queue(queues.split(",")[i]);
		}*/
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		container.setQueues(queue);
		container.setExposeListenerChannel(true);
		//同时存在的最大消费者
		container.setMaxConcurrentConsumers(1);
		container.setConcurrentConsumers(1);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		container.setMessageListener(new ReciveQueueConsumerMessageListener());
		return container;
	}
	
	
}
