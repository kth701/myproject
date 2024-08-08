package com.spring.myproject.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    //http.csrf( c-> c.disable());

    // 2. 인증 과정 처리

    // 2.1 로그인 관련 설정 => UserDetailsSeervice인터페이스 구현 후 설정 할 것
    http.csrf(AbstractHttpConfigurer::disable)
        .formLogin(login -> {
                    login.loginPage("/members/login")   // 로그인 처리할 url 설정
                        .defaultSuccessUrl("/")     // 로그인 성공시 url 설정
                        .usernameParameter("email") // 로그인 성공시 가져올  data 매개변수
                        .passwordParameter("password")  // 로그인 성공시 가져올 data매개변수
                        .failureUrl("/board/list")  ;  // 로그인 실패시 url 설정
                    });

    // SpringBoot 3v 변경된 코드 확인 authorizeRequests() → authorizeHttpRequests()
    //http.authorizeRequests().anyRequest().authenticated(); -> http.authorizeHttpRequests().anyRequest().authenticated();

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

인증(Authentication)과 username
- 사용자의 아이디만으로 사용자의 정보를 로딩
- 로딩된 사용자의 정보를 이용해서 패스워드 검증

UserDetailsService인터페이스 : 인증을 처리하는 인터페이스 구현체
 -  loadUserByUsername() 1개의 메서드만 존재 : 실제 인증을 처리할 때 수행되는 메서드



 http.authorizeRequests()
                // image 폴더를 login 없이 허용
                .antMatchers("/images/**").permitAll()
                // css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
                // 어떤 요청이든 '인증'
                .anyRequest().authenticated()
                .and()
                    // 로그인 기능 허용
                    .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/user/login?error")
                    .permitAll()
                .and()
                    // 로그아웃 기능 허용
                    .logout()
                    .permitAll();

 */
