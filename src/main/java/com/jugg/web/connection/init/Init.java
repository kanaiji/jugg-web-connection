package com.jugg.web.connection.init;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.jugg.web.connection.core.ConnectionPersist;
import com.jugg.web.connection.core.ConnectionTask;
import com.jugg.web.connection.core.beanfactory.SpringContextUtil;

/**
 * 功能说明：处理业务
 */
@WebServlet(name="init-app", urlPatterns= {"/init/*"}, loadOnStartup=2)
public class Init extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(Init.class);

	public static final TransferQueue<ConnectionTask> tasks = new LinkedTransferQueue<ConnectionTask>();

	private ThreadPoolTaskExecutor threadPool;

	public static ConnectionPersist connectionPersist;
	
	public  SpringContextUtil springContextUtil;

	@Override
	public void init() throws ServletException {
		
		try {
			springContextUtil = new SpringContextUtil();
			threadPool = (ThreadPoolTaskExecutor) springContextUtil.getBean("threadPool");
			connectionPersist = (ConnectionPersist) springContextUtil.getBean("connectionPersist");
//			connectionPersist.initPushDataLogFromDB(); // 从数据库中取一次数据用来当系统启动时初始化（此处可优化）

			startThread(); // 启动任务处理线程

			logger.warn("connection|starting...successful");
		} catch (Exception e) {
			logger.error("connection|application start error:", e);
		}finally{
			//提前GC回收。
			springContextUtil=null;
		}
	}
	
	/**
	 * 优化 线程的销毁 和 开辟所浪费的资源 PS : 2核 的cpu ,最大线程数设置10 ，核心设置为2 ，缓冲队列也是10000
	 */
	private void startThread() {
		logger.warn("connection|startThread...");

//		for (int i = 0; i < 1; i++) {
			for (int i = 0; i < threadPool.getMaxPoolSize(); i++) {

			threadPool.execute(new Runnable() {
				
				/** 当前执行次数 **/
			    int executeCount = 0;
				  
			    /** 默认最大执行次数 3 **/
			    int executeMaxCount = 3;
			    
				public void run() {
					try {
						while (true) {
							ConnectionTask task = null ;
							if(executeCount == 0){
								Thread.sleep(1000);
//								Thread.sleep(10000);
								task = tasks.poll(); // 使用take方法获取过期任务,如果获取不到,就一直等待,知道获取到数据并删除
								logger.warn("connection|executeCount={}|剩余大小==>tasks.size={}:", executeCount, tasks.size());
							}
							if (task != null) {
								task.run(executeCount, executeMaxCount); // 执行更新map
							}
						}
					} catch (Exception e) {
						logger.error("connection|task run error;", e);
					}
				}
			});

		}
	}

}
