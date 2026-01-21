package com.kosa.fillinv.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://localhost:8080/resources/files/ 하위에 접근 시 classpath:/files/ 경로에서 리소스를 제공
        registry.addResourceHandler("/resources/files/**")
                .addResourceLocations("classpath:/files/");
    }
}