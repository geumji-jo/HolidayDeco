package com.hdd.hdeco.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hdd.hdeco.intercept.AutologinIntercepter;
import com.hdd.hdeco.intercept.LoginCheckInterceptor;
import com.hdd.hdeco.intercept.PreventLoginInterceptor;
import com.hdd.hdeco.intercept.PwCheckInterceptor;
import com.hdd.hdeco.intercept.SleepUserCheckInterceptor;
import com.hdd.hdeco.util.MyFileUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	//field 
	private final LoginCheckInterceptor loginCheckInterceptor;
	private final PreventLoginInterceptor preventLoginInterceptor;
	private final AutologinIntercepter autologinIntercepter;
	private final SleepUserCheckInterceptor sleepUserCheckInterceptor;
	private final PwCheckInterceptor pwCheckInterceptor;
	private final MyFileUtil myFileUtil;

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginCheckInterceptor)
						.addPathPatterns("/user/logout.do", "/user/out.do","/user/out.html","/user/myPageHome.html","/user/modifyInfo.html", "/user/checkPw.html"); // 모든 요청 
						//.excludePathPatterns=("/user/leave.do"); // 제외할 요청 
		
		registry.addInterceptor(autologinIntercepter)
						.addPathPatterns("/**");

		registry.addInterceptor(preventLoginInterceptor)
						.addPathPatterns("/user/login.html", "/user/agree.html","/user/join.html","/user/join.do","/user/findId.html","/user/findPw.html","/user/naver_join.html");
		
		registry.addInterceptor(sleepUserCheckInterceptor)
						.addPathPatterns("/user/login.do");
		
		registry.addInterceptor(pwCheckInterceptor)
						.addPathPatterns("/user/modifyInfo.html");
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/imageLoad/**")
	            .addResourceLocations("file:" + myFileUtil.getSummernoteImagePath() + "/");
	    
	    registry.addResourceHandler("/admin/images/itemImg/**") // admin 페이지에서 사용하는 이미지 경로 설정
	            .addResourceLocations("file:" + myFileUtil.getPath() + "/"); // 업로드된 이미지가 저장된 경로 설정
	}

}
