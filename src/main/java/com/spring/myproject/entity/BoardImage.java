package com.spring.myproject.entity;


public class BoardImage {
}

/*
엔티티 매핑시 방향성으로 고려할 것
테이블관계: 항상 양방향, 객체: 단방향, 양방향

연관관계 종류
일대일(1:1) @OneToOne
 - 회원, 장바구니 관계

일대다(1:N) @OneToMany
 - 1개의 게시글, 여러 첨부파일
 - 1개의 장바구니, 여러 상품

다대일(N:1) @ManyToOne
 - 여러 첨부파일 , 1개의 게시글
 - 여러 상품, 1개의 장바구니

일대일(N:M) @ManyToMany




 */