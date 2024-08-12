package com.spring.myproject.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class AuthMemberDTO extends User {

  private String name;
  private String email;
  //private String password; // 부모요소에 있는 password필드 그대로 사용
  private String address;

  public AuthMemberDTO( // 생성자에 전달 받는 매개변수와 인자값
                String address,     // member.getAddress
                String name,        // member.getName

                String username,    //member.getEmail
                String password,    // member.getPassword
                Collection<? extends GrantedAuthority> authorities // member.getRoleSet()
                ) {

    super(username, password, authorities); // 부모 생성자 호출

    this.name   = name;
    this.email  = username;
    this.address = address;
  }

}