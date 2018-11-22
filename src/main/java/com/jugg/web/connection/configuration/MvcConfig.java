package com.jugg.web.connection.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

 
/**
 * 拦截器配置
 *
 *
 * @Auther: agui
 * @Date: 2018/7/11 15:05
 */
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
	
	
//    以下WebMvcConfigurerAdapter 比较常用的重写接口
//    /** 解决跨域问题 **/
//    public void addCorsMappings(CorsRegistry registry) ;
//    /** 添加拦截器 **/
//    void addInterceptors(InterceptorRegistry registry);
//    /** 这里配置视图解析器 **/
//    void configureViewResolvers(ViewResolverRegistry registry);
//    /** 配置内容裁决的一些选项 **/
//    void configureContentNegotiation(ContentNegotiationConfigurer configurer);
//    /** 视图跳转控制器 **/
//    void addViewControllers(ViewControllerRegistry registry);
//    /** 静态资源处理 **/
//    void addResourceHandlers(ResourceHandlerRegistry registry);
//    /** 默认静态资源处理器 **/
//    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer);
 
    /**
     *
     * 功能描述:
     *  配置静态资源,避免静态资源请求被拦截
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("/templates/");
//        放行swagger
        /*registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");*/
        super.addResourceHandlers(registry);
    }
 
   /* public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                //addPathPatterns 用于添加拦截规则
                .addPathPatterns("/**")
                //excludePathPatterns 用于排除拦截
                //注意content-path：ding是不用填写的
                //忽略swagger
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
                //放开登录
                .excludePathPatterns("/login/**");
                //发送验证码
                .excludePathPatterns("/send_auth_code")
                //获取用户基本信息
                .excludePathPatterns("/get_user_base_info")
                //获取用户详情
                .excludePathPatterns("/get_user_detail_info");
        super.addInterceptors(registry);
    }*/
    
    
    
    /**
     * 解决跨域问题
     */
    public void addCorsMappings(CorsRegistry registry){
    	registry.addMapping("/**");
//		.allowedOrigins("http://localhost:8082")
//		.allowedMethods("GET", "POST")
//		.allowCredentials(false).maxAge(3600);
    	
    }
    
    
    /**
	 * 配置JSP视图解析器
	 * 如果没有配置视图解析器。Spring会使用BeanNameViewResolver，通过查找ID与逻辑视图名称匹配且实现了View接口的beans
	 * 
	 * @return
	 */
    private static final String prefix = "/WEB-INF/view/";
    private static final String suffix = ".jsp";
	@Bean
	public InternalResourceViewResolver setupViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		/** 设置视图路径的前缀 */
		resolver.setPrefix(prefix);
		/** 设置视图路径的后缀 */
		resolver.setSuffix(suffix);
		return resolver;

	}
    
    
    
}
