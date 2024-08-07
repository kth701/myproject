package com.spring.myproject.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Log4j2@RequiredArgsConstructor
public class CustomSecurityConfig {
  @Bean
  public BCryptPasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }



  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    // SecurityFilterChain의 filterChain(): 모든 사용자가 모든 경로에 접근할 수 있게 설정
    log.info("=> SecurityFilterChain() 호출");

    // 1. CSRF요청 비활성화: 개발테스트 비활성화
    http.csrf( c-> c.disable());

    // 2. 인증 과정 처리
    // 3. 로그아웃 관련 설정

    return http.build();


  }


}

/*
인증이 필요 없는 경우 : 상품 상세 페이지 조회
인증이 필요한 경우:  상품 주문
관리자 권한 필요한 경우: 상품 등록

인증(Authentication) : 신원 확인 개념
인가(Authorization) : 허가나 권한 개념

 */
