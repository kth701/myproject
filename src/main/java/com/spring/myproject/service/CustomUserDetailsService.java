package com.spring.myproject.service;


import com.spring.myproject.dto.AuthMemberDTO;
import com.spring.myproject.entity.Member;
import com.spring.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     log.info("=> loadUserByUsername: "+ username);

     // 1. 더미 User객체 생성하기
    /*
    UserDetails userDetails = User.builder()
        .username("admin1@gmail.com")
        .password(passwordEncoder.encode("1234")) // 패스워드 인코드 필요
        .authorities("ROLE_USER")
        .build();
    */

    // 2. Member Entity(DB)에 있는 정보를 기준으로 Authentication 처리
    Member member = memberRepository.findByEmail(username);
    log.info("==> member.getRoleSet():"+"ROLE_"+member.getRoleSet().toString());

    if (member == null){ // 미가입 회원일 경우
      throw new UsernameNotFoundException(username);
    }

    // 2-1. db에 있는 회원정보를 User객체에 정보를 저장한후 UserDetails객체 생성
    /*
    UserDetails userDetails = User.builder()
        .username(member.getEmail())
        .password( member.getPassword())    // 암호화 인코드 생략
        // ROLE_ADMIN, ROLE_USER, ROLE_XXX
        .authorities("ROLE_"+member.getRole().toString())
        .build();

    return userDetails;
     */

    // 2-2 db에 있는 회원정보를 User객체로부터 상속받은 AuthMemberDTO객체 정보로 저장한 후
    // UserDetails객체 생성
    log.info("==> Member info: "+member);

    AuthMemberDTO authMember = new AuthMemberDTO(
        member.getAddress(),
        member.getEmail(),
        member.getPassword(),
        member.getRoleSet()
            .stream()
            .map( role ->
                new SimpleGrantedAuthority("ROLE_" + role.name())
            ).collect(Collectors.toList())
        );

    log.info("==> authMemberDTO: "+authMember);

    return authMember;
  } // end loadUserByUsername()

}

/*
UserDetailsService: DB에서 회원 정보를 가져오는 역할
loadUserByUsername(): 회원정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails인터페이스 반환
UserDetails: 시큐리티에서 회원 정보를 담기위해서 사용되는 인터페이스 , 직접구현 or 시큐리티에서 제공하는 User클래스 사용
 */