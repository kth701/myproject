package com.spring.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor@Builder
public class MemberDTO {
  private String name;
  private String email;
  private String password;
  private String address;
}
