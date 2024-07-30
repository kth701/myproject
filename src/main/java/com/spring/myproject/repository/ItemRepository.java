package com.spring.myproject.repository;

import com.spring.myproject.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,
                                        QuerydslPredicateExecutor<Item> {

  // 1. spring data jpa query 메서드
  // 형식 : findBy엔티티필드명()
  // 상품명으로 조회
  List<Item> findByItemNm(String itemNm);
  // 상품명과 상품 상세 설명을 OR 조건 조회
  List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
  // 상품 가격이 전달된 매개변수보다 값이 작은 상품 조회
  List<Item> findByPriceLessThan(Integer price); // price < 매개변수값
  // 조건 조회 후 정렬
  List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);


  // 2. spring data jpa @query어노테이션 => 파라미터이름 => '%:매개변수%'
  @Query("select i from Item i where i.itemDetail like %:itemDetail%  order by i.price desc")
  List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

  // 기존이 데이터베이스에 사용하던 쿼리문 그대로 사용시=> (쿼리문, nativeQuery속성) 필수
  @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc",
         nativeQuery = true)
  List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}

/*
Querydsl장점
- 고정된 SQL문이 아니 조건에 맞게 동적 쿼리를 생성
- 비슷한 쿼리를 재사용, 제약 조건 조립 및 가독성 향상
- 문자열이 아닌 자바 소스코드로 작성하기 때문에 컴파일 시점에 오류생성
- IDE도움을 받아 자동 완성 기능 활용


QueryDslPredidicateExecuter적용하여 상품 조회 기능 구현
(Priedicate: 조건이 맞는지 판별)

Repositiory에서는 Predicate를 파라미터로 전달하기위해
 => QueryDslPredicateExecutor인터페이스를 상속

 */