package com.spring.myproject.repository;

import com.spring.myproject.entity.Board;
import com.spring.myproject.repository.search.BoardSearch;
import jakarta.persistence.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends
                                  JpaRepository<Board, Long>,
                                  // Board검색기능 상속(BoardSearch)
                                  BoardSearch {

  // 메서드 상속 받음 (extends BoardSearch)
  // Page<Board> search2(Pageable pageable);


  // Board와 BoardImage 연관관계 : @OneToMany설정시 => BoardImage는 지연 로딩 상태
  // @OneToMany: 기본적으로 지연로딩을 이용 :  fetch = FetchType.LAZY

  // @EntityGraph: 지연로딩일지라도 한 번에 조인 처리해서 select처리하도록 설정
  @EntityGraph(attributePaths = {"imageSet"})
  @Query("select b from Board b where b.bno = :bno")
  Optional<Board> findByIdWidthImages(Long bno);

}









/*

JpaRepository에서 지원하는 메서드

<S extends T> save(S entity)    : 엔티티 저장 및 수정
void delete(t entity)           : 엔티티 삭제
count()                         : 엔티티 총 개수 반환
Iterable<T> finaAll(0)          : 모든 엔티티 조회

 */