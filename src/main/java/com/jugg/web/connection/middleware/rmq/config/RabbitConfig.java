package com.jugg.web.connection.middleware.rmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.jugg.web.connection.middleware.rmq.producter.MsgSendConfirmCallBack;
import com.jugg.web.connection.middleware.rmq.producter.MsgSendReturnCallback;

@Configuration
@Service("rabbitConfig")
public class RabbitConfig {
	
	private Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
	
	@Value("${spring.rabbitmq.host}")
	private String mqRabbitHost;
	
	@Value("${spring.rabbitmq.port}")
	private String mqRabbitPort;
	
	@Value("${spring.rabbitmq.username}")
	private String mqRabbitUserName;
	
	@Value("${spring.rabbitmq.password}")
	private String mqRabbitPassword;
	
	@Value("${spring.rabbitmq.virtual-host}")
	private String mqRabbitVirtualHost;
	
	@Value("${spring.rabbitmq.publisher-confirms}")
	private String mqRabbitPublisherConfirms;
	
	@Bean
	public ConnectionFactory connectionFactory() {
	    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.mqRabbitHost,Integer.valueOf(this.mqRabbitPort));
	    connectionFactory.setUsername(this.mqRabbitUserName);
	    connectionFactory.setPassword(this.mqRabbitPassword);
	    connectionFactory.setVirtualHost(this.mqRabbitVirtualHost);
	    connectionFactory.setPublisherConfirms(Boolean.valueOf(this.mqRabbitPublisherConfirms));

	    return connectionFactory;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
	    RabbitTemplate template = new RabbitTemplate(connectionFactory());
	    template.setConfirmCallback(new MsgSendConfirmCallBack());//消息确认
	    template.setReturnCallback(new MsgSendReturnCallback());//确认后回调

	    return template;
	}
	
	
	
	
	
	/******************* rmq 队列初始化 *************************************/
	//jugg constemer's queue
	public final static String connectionReceiveQueueName = "connection-receive-queue";
	
	public final static String connectionResultQueueName = "connection-result-queue";
	
	public final static String runErrorQueueName = "run-error-queue";

	@Bean
    public Queue connectionReceiveQueue() {
    	logger.info("RMQ-PRODUCTER|info|config-init|connectionReceiveQueue()..queueName: {}", connectionReceiveQueueName);
        return new Queue(connectionReceiveQueueName, true);
    }
	
    @Bean
    public Queue connectionResultQueue() {
    	logger.info("RMQ-PRODUCTER|info|config-init|connectionResultQueue()..queueName: {}", connectionResultQueueName);
        return new Queue(connectionResultQueueName, true);
    }
    
    @Bean
    public Queue runErrorQueue() {
    	logger.info("RMQ-PRODUCTER|info|config-init|runErrorQueue()..queueName: {}", runErrorQueueName);
        return new Queue(runErrorQueueName, true);
    }
    
    
    /******************* rmq 路由初始化 *************************************/
    
    public final static String connectionResultExchangeName = "connection-result-direct-route";
    
    public final static String runErrorExchangeName = "run-error-direct-route";
    
    
    @Bean
    DirectExchange connectionResultDirectExchange() {
    	logger.info("RMQ-PRODUCTER|info|config-init|connectionResultDirectexChange()..route: {}", connectionResultExchangeName);
        return new DirectExchange(connectionResultExchangeName);
    }
    
    @Bean
    DirectExchange runErrorDirectExchange() {
    	logger.info("RMQ-PRODUCTER|info|config-init|runErrorDirectExchange()..route: {}", runErrorExchangeName);
        return new DirectExchange(runErrorExchangeName);
    }
    
    
   
    /******************* rmq 路由队列绑定 *********************************/
    
    public final static String connectionResultRouteName = "connection-result-route-direct-queue";
    
    public final static String runErrorRouteName = "run-error-route-direct-queue";
    
    @Bean
    public Binding bindingResult() {
    	logger.info("RMQ-PRODUCTER|info|config-init|bindingExchange()..DirectExchange");
        return BindingBuilder.bind(connectionResultQueue()).to(connectionResultDirectExchange()).with(connectionResultRouteName);
    }

    @Bean
    public Binding bindingError() {
    	logger.info("RMQ-PRODUCTER|info|config-init|bindingError()..DirectExchange");
        return BindingBuilder.bind(runErrorQueue()).to(runErrorDirectExchange()).with(runErrorRouteName);
    }

}
