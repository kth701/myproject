package com.spring.myproject.repository;

import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Cart;
import com.spring.myproject.entity.Member;
import com.spring.myproject.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional  // Test후 Rollback처리됨.
@Log4j2
@TestPropertySource(locations = {"classpath:application-test.properties"})
class CartRepositoryTest {

  @Autowired
  CartRepository cartRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  MemberService memberService;
  @Autowired
  PasswordEncoder passwordEncoder;

  @PersistenceContext
  EntityManager em;

  // 회원 등록 메서드
  public Member createMember(){

    // dto 생성
    MemberDTO memberDTO = MemberDTO.builder()
        .email("test@email.com")
        .name("홍길동")
        .address("부산시")
        .password("1111")
        .build();

    // dto -> entity
    return memberService.dtoToEntity(memberDTO, passwordEncoder);
  }

  @Test
  @DisplayName("장바구니와 회원Entity 맵핑 조회")
  public void findCartAndMemberTest(){

    Member member = createMember();// member entity생성하는 메서드 호출
    memberRepository.save(member);

    Cart cart = new Cart();// cart 객체 생성
    cart.setMember(member); // cart의 member객체 입력(설정)(

    cartRepository.save(cart);

    em.flush();// db에 반영
    em.clear();// 영속성 컨텍스트 비우기

    Cart savedCart = cartRepository
        .findById(cart.getId())
        .orElseThrow(EntityNotFoundException::new);

    assertEquals(savedCart.getMember().getId(), member.getId());

  }

}
