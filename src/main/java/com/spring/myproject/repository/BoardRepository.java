package com.spring.myproject.repository;

import com.spring.myproject.entity.Board;
import com.spring.myproject.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {


  // 메서드 상속 받음 (extends BoardSearch)
  // Page<Board> search2(Pageable pageable);

}









/*

JpaRepository에서 지원하는 메서드

<S extends T> save(S entity)    : 엔티티 저장 및 수정
void delete(t entity)           : 엔티티 삭제
count()                         : 엔티티 총 개수 반환
Iterable<T> finaAll(0)          : 모든 엔티티 조회

 */