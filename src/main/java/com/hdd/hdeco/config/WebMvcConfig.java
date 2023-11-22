package com.hdd.hdeco.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hdd.hdeco.intercept.LoginCheckInterceptor;
import com.hdd.hdeco.util.MyFileUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	//field 
	private final LoginCheckInterceptor loginCheckInterceptor;
	private final MyFileUtil myFileUtil;

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginCheckInterceptor)
		//.addPathPatterns("/bbs/write.html", "/upload/write.html")
		.addPathPatterns("/user/logout.do");
	registry.addInterceptor(loginCheckInterceptor)
	.addPathPatterns("/**") // 모든 요청 
	.excludePathPatterns("/user/leave.do"); // 제외할 요청 
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/imageLoad/**")
		 .addResourceLocations("file:" + myFileUtil.getSummernoteImagePath() + "/");
	}
}
