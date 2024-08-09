package com.spring.myproject.repository;

import com.spring.myproject.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  // 지연로딩시 적용 : 쿼리실행시 proxy객체 생성하여 proxy객체 호출시에만 수행
  @EntityGraph(attributePaths = "roleSet")
  Member findByEmail(String email);
}
