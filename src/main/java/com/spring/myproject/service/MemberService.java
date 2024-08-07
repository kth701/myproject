package com.spring.myproject.service;


import com.spring.myproject.constant.Role;
import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.dto.ReplyDTO;
import com.spring.myproject.entity.Board;
import com.spring.myproject.entity.Member;
import com.spring.myproject.entity.Reply;
import com.spring.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface MemberService {

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
    member.setRole(Role.USER);

    return member;
  }
}
