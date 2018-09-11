package com.jugg.web.connection.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Configuration
@Service("threadPoolConfig")
public class ThreadPoolConfig {
	
    @Bean
	public ThreadPoolTaskExecutor threadPool() {
		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		threadPool.setCorePoolSize(2);
		threadPool.setKeepAliveSeconds(10000);
		threadPool.setMaxPoolSize(4);
		threadPool.setQueueCapacity(6);
	    return threadPool;
	}

}
