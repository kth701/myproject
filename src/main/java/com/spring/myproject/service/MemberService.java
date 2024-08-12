package com.spring.myproject.service;


import com.spring.myproject.constant.Role;
import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberService  {

  public Member saveMember(MemberDTO memberDTO);

  // 1. 방법 : dtoToEntity : MemberDTO -> 암호화 -> Entity
  default Member dtoToEntity(MemberDTO memberDTO,  PasswordEncoder passwordEncoder){
    Member member = new Member();

    member.setName(memberDTO.getName());
    member.setEmail(memberDTO.getEmail());
    member.setAddress(memberDTO.getAddress());

    // 비밀번호 -> 암화화 작업
    String password = passwordEncoder.encode(memberDTO.getPassword());
    member.setPassword(password);
    // 1. 권한 설정이 1개일 경우
    //member.setRole(Role.USER);
    //member.setRole(Role.ADMIN);

    // 2. 권한 설정이 여러개 일 경우
    member.addRole(Role.USER);

    return member;
  }
}
