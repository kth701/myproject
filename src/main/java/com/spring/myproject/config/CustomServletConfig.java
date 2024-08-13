package com.spring.myproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Swagger UI 라이브러리로 인하여 정적자원 경로가 변경됨
// 정적자원 경로를 재설정 시킴
@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //WebMvcConfigurer.super.addResourceHandlers(registry);

    registry.addResourceHandler("/js/**")
        .addResourceLocations("classpath:/static/js/");
    registry.addResourceHandler("/css/**")
        .addResourceLocations("classpath:/static/css/");
    registry.addResourceHandler("/")
        .addResourceLocations("classpath:/static/");
  }
}
