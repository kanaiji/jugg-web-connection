package com.jugg.web.connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//在部署到外部的Tomcat时，需要将classpath的引入文件去掉，因为在web.xml已经配置过一次了

//解決，当spring创建dataSource bean因缺少相关的信息就会报错。 implements CommandLineRunner 
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableConfigurationProperties
public class Application extends SpringBootServletInitializer{
	// 入口
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// Java EE应用服务器配置，
	// 如果要使用tomcat来加载jsp的话就必须继承SpringBootServletInitializer类并且重写其中configure方法
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public void run(String... args) throws Exception {
		System.out.println("springboot启动完成！");
	}
	
	
	
	
	
	

}
