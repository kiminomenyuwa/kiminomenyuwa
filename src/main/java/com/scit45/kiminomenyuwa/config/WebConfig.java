package com.scit45.kiminomenyuwa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 프로필 사진 저장 경로에 접근하기 위한 Configuration
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// "file:/c:/upload/" 경로를 "/files/" URL로 매핑
		registry.addResourceHandler("/files/**")
			.addResourceLocations("file:/c:/upload/");
	}
}
