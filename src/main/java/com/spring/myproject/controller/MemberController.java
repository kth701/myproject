package com.spring.myproject.controller;


import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Member;
import com.spring.myproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;

  // 회원 등록: GET, POST
  @GetMapping(value="/new")
  public String memberRegisterForm(Model model){
    // 데이터가 없는 memberDTO생성 : form에 입력한 데이터와 1:1 맵핑
    model.addAttribute("memberDTO", new MemberDTO());

    // 포워딩: 뷰리졸브
    return "members/memberForm";
  }
  // @ModelAttribute: 다양한 소스의 데이터를 모델 특성으로 바인딩하는 데 사용
  // @RequestBody: HTTP request body를 메소드에 매핑하는데 사용
  @PostMapping(value="/new")
  public String memberRegister(@Valid @ModelAttribute  MemberDTO memberDTO,
                               BindingResult bindingResult,
                               Model model){

    log.info("=> "+memberDTO);

    if (bindingResult.hasErrors()){// 유효성 검사결과 1개이상 에러가 있으면 처리
      log.info("=> "+ bindingResult.toString());

      return "members/memberForm";
    }

    try {
      // dto -> entity -> email중복 체크 ->  save
      memberService.saveMember(memberDTO);
    } catch(Exception e){ // 중복된 이메일 등록시 예외발생되는 Exception 처리
      model.addAttribute("errorMessage",e.getMessage());
      return "members/memberForm";// 입력폼으로 포워딩
    }

    return "redirect:/";
  }



  //----------------------- //
  // 로그인, 로그아웃 처리
  //----------------------- //
  // 1. 로그인
  @GetMapping(value="/login")
  public String loginMember(String error, String logout){
    log.info("=> login ");

    // 로그인 폼이 있는 페이지로 포워딩
    return "/members/loginForm";
  }
  // 1-1. 로그인 실패시 처리할 url
  @GetMapping("/login/error")
  public String loginError(Model model){
    log.info("==> login error");

    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호 확인해주세요.");
    return "/members/loginForm";

  }


}
