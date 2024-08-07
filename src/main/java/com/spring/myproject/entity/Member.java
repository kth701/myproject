package com.spring.myproject.entity;


import com.spring.myproject.constant.Role;
import com.spring.myproject.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name="member")
@Getter@Setter@ToString
public class Member {
  @Id
  @Column(name="member_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @Column(unique = true)  // 회원은 이메일에 동일한 값이 허용할 수 없도록 설정
  private String email;
  private String password;
  private String address;
  @Enumerated(EnumType.STRING)
  private Role role;


  // createMember():  dto -> entity
  public static Member createMember(MemberDTO memberDTO,
                                    PasswordEncoder passwordEncoder){
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
