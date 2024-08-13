package com.spring.myproject.config;


import com.spring.myproject.security.Custom403Handler;
import com.spring.myproject.service.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@Log4j2
@RequiredArgsConstructor
// @EnableGlobalMethodSecurity ->  @EnableMethodSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // 어노테이션 권한 설정
public class CustomSecurityConfig {

  // 4-1. 자동 로그인에 필요한 객체 주입
  private final DataSource dataSource;
  private final CustomUserDetailsService customUserDetailsService;

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

    // 4-2. 자동 로그인에 필요한 설정

    http.rememberMe(rememberMe ->
        rememberMe
            .key("12345678")
            .tokenRepository(persistentTokenRepository()) // rememberMe쿠키의 값을 인코딩하기 위한 키(key), 필요한 정보를 저장하는 tokenRepository를 지정
            .userDetailsService(customUserDetailsService)
            .tokenValiditySeconds(10)  ); // rememberMe()
            //.tokenValiditySeconds(60*60*24*30)); // 30일 유효(초*분*시간*일)
            //.rememberMeParameter("remember") // 생략시 기본파라미터 명은 "remember-me", <input type='checkbox' name='파라미터이름'>
            //.alwaysRemember(true) // 리멤버 미 기능이 활성화되지 않아도 항상 실행

//    http
//        .rememberMe( rememberMe -> rememberMe.tokenValiditySeconds(10));


    // 2. 인증 과정 처리
    // 2.1 로그인 관련 설정 => UserDetailsSeervice인터페이스 구현 후 설정 할 것
    http.csrf(AbstractHttpConfigurer::disable)
        .formLogin(login -> {
                    login.loginPage("/members/login")             // 로그인 처리할 url 설정
                        .defaultSuccessUrl("/board/list")         // 로그인 성공시 url 설정
                        .usernameParameter("email")               // 웹의 username의  매개변수이름 설정
                        .passwordParameter("password")            // 웹의 password의  매개변수이름 설정
                        //.loginProcessingUrl("/members/login")   // 웹 로그인창의 form action값 설정
                        .failureUrl("/members/login/error")               // 로그인 실패시 url 설정

                        // 성공 또는 실패할 경우 핸들러 사용해서 원하는 것을 실행 할 경우 적용
                        // defaultSuccessUrl(),failureUrl() 중복될 경우 핸들러가 우선으로 수행됨.
                        .successHandler(new AuthenticationSuccessHandler() {
                          @Override
                          public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            log.info("==> authentication: "+authentication.getName());
                            response.sendRedirect("/board/list");
                          }
                        })
                        .failureHandler(new AuthenticationFailureHandler(){
                          @Override
                          public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                            log.info("==> exception: " + exception.getMessage());
                            response.sendRedirect("/members/login/error");
                            }
                          });
                    });

    // SpringBoot 3v 변경된 코드 확인 authorizeRequests() → authorizeHttpRequests()
    //http.authorizeRequests().anyRequest().authenticated(); -> http.authorizeHttpRequests().anyRequest().authenticated();
//    http.authorizeHttpRequests( auth -> {
//        auth.requestMatchers("/","/members/**").permitAll();
//        auth.requestMatchers("/board/**").hasRole("ADMIN");
//        auth.anyRequest().permitAll();
//    });
//    http
//        .authorizeRequests()
//        .requestMatchers("/","/css/**","/js/**","/members/**").permitAll()
//        .requestMatchers("/board/list").hasRole("ADMIN")
//        .requestMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
//        .anyRequest().authenticated();


    // 3. 로그아웃 관련 설정
    // 로그아웃을 기본으로 설정 => url : "/logout" 로그아웃 수행
    //http.logout(Customizer.withDefaults());
    http.logout(logout -> {
      logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
          .logoutSuccessUrl("/")
          .invalidateHttpSession(true);
    });

    // 5. 접근권한에 맞지 않은 요청시 403에러 핸들러 처리
    http.exceptionHandling(
        e -> e.accessDeniedHandler(accessDeniedHandler())
    );


    return http.build();
  }

  // 4-3. 자동 로그인: 토큰
  @Bean
  public PersistentTokenRepository persistentTokenRepository(){

    JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
    repo.setDataSource(dataSource);

    return repo;
  }

  // 5-1. 접근권한에 맞지 않은 요청시 403에러 핸들러 처리
  @Bean
  public AccessDeniedHandler accessDeniedHandler(){
    return new Custom403Handler();
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

/* 어노테이션 권한 설정
@EnableGlobalMethodSecurity(
        securedEnabled = true,  // @Secured어노테이션 확성화 여부
        prePostEnabled = true   // @PreAuthorized, @PostAuthorized 어노테이션 활성화 여부
        )



 */


/*
자동 로그인 : 데이터베이스 적용=> 테이블 이름은 "persistent_logins" 으로 사용해야함.

 CREATE TABLE persistent_logins (
 	username VARCHAR(64) NOT NULL,
 	series 	VARCHAR(64) PRIMARY KEY,
 	token 	VARCHAR(64) NOT NULL ,
 	last_used TIMESTAMP 	NOT NULL
 );


 */