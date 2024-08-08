package com.spring.myproject.service;


import com.spring.myproject.entity.Member;
import com.spring.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     log.info("=> loadUserByUsername: "+ username);

     // 더미 User객체 생성하기
    /*
    UserDetails userDetails = User.builder()
        .username("user1@test.com")
        .password(passwordEncoder.encode("1234")) // 패스워드 인코드 필요
        .authorities("ROLE_USER")
        .build();
     */

    // Member Entity(DB)에 있는 정보를 기준으로 Authentication 처리
    Member member = memberRepository.findByEmail(username);

    if (member == null){ // 미가입 회원일 경우
      throw new UsernameNotFoundException(username);
    }

    UserDetails userDetails = User.builder()
        .username(member.getEmail())
        .password( member.getPassword())    // 암호화 인코드 생략
        .authorities(member.getRole().toString())
        .build();

    return userDetails;
  }
}

/*
UserDetailsService: DB에서 회원 정보를 가져오는 역할
loadUserByUsername(): 회원정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails인터페이스 반환
UserDetails: 시큐리티에서 회원 정보를 담기위해서 사용되는 인터페이스 , 직접구현 or 시큐리티에서 제공하는 User클래스 사용
 */