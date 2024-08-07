package com.spring.myproject.repository;

import com.spring.myproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);
}
