package com.feiqu.framwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * web mvc配置
 *
 * @author yanni
 * @date 2021/11/28
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( new VisitsInterceptor()).addPathPatterns("/**").excludePathPatterns("/blackList/denyService");
    }
    
	/**
	 * 跨域访问
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		/*registry.addMapping("/api*//**")
		.allowedOrigins("http://ibeetl.com","http://www.ibeetl.com")
		.allowedMethods("*");*/
		
	}
}
