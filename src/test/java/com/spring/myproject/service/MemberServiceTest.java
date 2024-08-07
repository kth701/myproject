package com.spring.myproject.service;

import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = {"classpath:application-test.properties"})
class MemberServiceTest {

  @Autowired
  MemberService memberService;
  @Autowired
  PasswordEncoder passwordEncoder;

  // 회원 정보 DTO, Entity생성하기
  public Member createMember(){
    // 클라이언트로부터 전달받은
    // 더미 data MemberDTO 생성
    MemberDTO memberDTO = MemberDTO.builder()
        .email("test@email.com")
        .name("홍길동")
        .address("부산시 진구")
        .password("1234")
        .build();

    //----------------------------------//
    // dto -> 암호화 작업 -> entity
    //----------------------------------//

    // 1. dto->entity : Member클래스 createMember메서드 활용
    //return Member.createMember(memberDTO, passwordEncoder);

    // 2. dto->entity :인터페이스에서 정의한 메서드 활용
    return memberService.dtoToEntity(memberDTO, passwordEncoder);
  }

  @Test
  @DisplayName("회원 가입 서비스 테스트")
  public void saveDuplicateMemberTest(){

    // 회원1
    Member member = createMember();
    Member savedMember = memberService.saveMember(member);

    // 회원 등록 테스트 결과 체크: assertEquals(기대값, 실제값)
    assertEquals(member.getEmail(),     savedMember.getEmail());
    assertEquals(member.getEmail(),     savedMember.getEmail());
    assertEquals(member.getAddress(),   savedMember.getAddress());
    assertEquals(member.getPassword(),  savedMember.getPassword());
    assertEquals(member.getRole(),      savedMember.getRole());

  }

  @Test
  @DisplayName("중복 회원 가입 테스트")
  public void saveMemberTest(){
    // 회원1
    Member member1 = createMember();
    memberService.saveMember(member1);
    // 회원2
    Member member2 = createMember();

    Throwable e = assertThrows(
        IllegalStateException.class, () ->{  memberService.saveMember(member2); }
    );

    // 예외 발생 메시지 동일 여부 확인
    assertEquals("이미 가입된 회원 입니다.",      e.getMessage());

  }

}