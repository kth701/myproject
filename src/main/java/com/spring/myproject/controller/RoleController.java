package com.spring.myproject.controller;


import com.spring.myproject.entity.Member;
import com.spring.myproject.repository.MemberRepository;
import com.spring.myproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {

  private final MemberRepository memberRepository;


  //---------------------------- //
  // 메서드단위로 권한 설정 여부 테스트
  //
  // CustomSecurityConfig 클래스에서
  // @EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
  // 어노데이션 선언
  //
  //--------------------------- //
  // 권한이 ADMIN인경우 허용
  @Secured(value={"ROLE_USER", "ROLE_ADMIN"}) // @Secured : 한개의 권한만 가능 (OR만가능)
  @GetMapping("/secured")
  public @ResponseBody String roleSe(){
    return "@Secured(value='ROLE_ADMIN')";
  }
  // 권한이 USER인 경우 허용
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user")
  public @ResponseBody String roleUser(){
    return "@PreAuthorize(hasRole('USER'))";
  }

  // 로그인 상태이면서 권한이 ADMIN이거나 USER인 경우 허용
  @PostAuthorize("isAuthenticated() and( hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') )")
  @GetMapping("/admin")
  public @ResponseBody String roleAdmin(){
    return "@PostAuthorize(ADMIN' 또는 'USER' 이고 isAuthenticated)";
  }

  @GetMapping("/test1")
  public @ResponseBody String test1(){
    return "/role/test => permitAll()";
  }
  @GetMapping("/test2")
  public @ResponseBody String test2(){
    return "/role/test => authenticated()";
  }
  @GetMapping("/test3")
  public @ResponseBody String test3(){
    return "/role/test => hasRole('USER')";
  }
  @GetMapping("/test4")
  public @ResponseBody String test4(){
    return "/role/test => hasRole('ADMIN')";
  }

  // 로그인상태 이면서, USER권한을 하지고 있거나, 특정 유저인 경우
//  @PostAuthorize("isAuthenticated() and (returnObject.name==principal.username)")
//  @GetMapping("/selectOne/{username}")
//  public Member  roleSelectOneUser(@PathVariable("username") String username){
//    return memberRepository.findByEmail(username);
//  }

}

/*
@PreAuthorize: 메서드가 실행되기전 인증 절차 처리
@PostAuthorize: 메서드가 실행되고 나서 응답을 보내기전에 인증절차

표현식
- hasRole([role]) : 현재 사용자의 권한이 파라미터의 권한과 동일한 경우 true
- hasAnyRole([role1,role2]) : 현재 사용자의 권한디 파라미터의 권한 중 일치하는 것이 있는 경우 true
- principal : 사용자를 증명하는 주요객체(User)를 직접 접근할 수 있다.
- authentication : SecurityContext에 있는 authentication 객체에 접근 할 수 있다.
- permitAll : 모든 접근 허용
- denyAll : 모든 접근 비허용
- isAnonymous() : 현재 사용자가 익명(비로그인)인 상태인 경우 true
- isRememberMe() : 현재 사용자가 RememberMe 사용자라면 true
- isAuthenticated() : 현재 사용자가 익명이 아니라면 (로그인 상태라면) true
- isFullyAuthenticated() : 현재 사용자가 익명이거나 RememberMe 사용자가 아니라면 true

 */
