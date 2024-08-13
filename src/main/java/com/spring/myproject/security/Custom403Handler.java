package com.spring.myproject.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

// 403에러 처리하는 클래스: 사용자권한이 없는 경우, 특정조건이 맞지 않은 경우 등
@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.info("=> ACCESS DENIED ---");

    response.setStatus(HttpStatus.FORBIDDEN.value());;

    // JSON 요청했는지 확인
    String contnetType = request.getHeader("Content-Type");
    boolean jsonRequest = contnetType.startsWith("application/json");
    log.info("=> isJSON: "+jsonRequest);

    // 일반 request
    if (!jsonRequest){
      response.sendRedirect("/members/login?error=ACCESS_DENIED");
    }
  }
}
