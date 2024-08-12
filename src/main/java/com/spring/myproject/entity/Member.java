package com.spring.myproject.entity;


import com.spring.myproject.constant.Role;
import com.spring.myproject.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="member")
@Getter@Setter
@ToString(exclude = "roleSet")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
  @Id
  @Column(name="member_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(unique = true)  // 회원은 이메일에 동일`한 값이 허용할 수 없도록 설정
  private String email;
  private String password;
  private String address;

//  @Enumerated(EnumType.STRING)
//  private Role role;

  // User 객체 및 Authentication 기능
  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Role> roleSet = new HashSet<>();



  public void changePassword(String password){
    this.password = password;
  }
  public void changeEmail(String email){
    this.email = email;
  }
  public void addRole(Role role){
    this.roleSet.add(role);
  }
  public void clearRoles(){
    this.roleSet.clear();
  }



  // 1.엔티티 메서드 방법 : createMember():  dto -> entity
  public static Member createMember(MemberDTO memberDTO,
                                    PasswordEncoder passwordEncoder){
    Member member = new Member();

    member.setName(memberDTO.getName());
    member.setEmail(memberDTO.getEmail());
    member.setAddress(memberDTO.getAddress());

    // 비밀번호 -> 암화화 작업
    String password = passwordEncoder.encode(memberDTO.getPassword());
    member.setPassword(password);
    // member.setRole(Role.USER); // Set<Role> 사용전전
    member.addRole(Role.USER);    // Set<Role> 사용후

    return member;
  }

}
