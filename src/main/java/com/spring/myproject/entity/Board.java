package com.spring.myproject.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

// Entity 정의 : 테이블에 적용될 구조설계 정의하여 테이블과 entity 1:1 맵핑
@Entity@Table(name="board")
@Getter@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Board extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bno;

  @Column(length = 500, nullable = false)
  private String title;
  @Column(length = 2000, nullable = false)
  private String content;
  @Column(length = 50, nullable = false)
  private String writer;

  // 현재 로그인 사용자 이메일와 게시글 작성자 이메일 동일한지 판별하기위한 항목
  private String email;

  // 데이터 수정하는 메서드
  public void change(String title, String content){
    this.title = title;
    this.content = content;
  }


  // 첨부파일
  // 1. mappedBy속성, cascade:상위엔티티가 하위에티티를 관리,@OneToMany(생략시FetchType.LAZY설정됨)
  //  - 외래키가 아닌 엔티티를 주인(주체)로 설정 할 경우 => Board Entity 멤버변수(속성)로 연관관계 설정이 아닌 경우
  //  - 어떤 엔티티의 속성으로 매핑할 경우 => imageSet.order속성으로 연관관계 설정
  //  -  @OneToMany => FetchType.LAZY 기본값으로 설정됨.

  // 2. 고아객체: 부모엔티티와 연관관계가 끊어진 자식엔티티
  //    고아객체 제거 :  (orphanRemoval속성=true)
  //    @OneToOne, @OneToMany에 사용
  @OneToMany(mappedBy = "board",
             cascade = {CascadeType.ALL},
             fetch = FetchType.LAZY,
             orphanRemoval = true  )
  @Builder.Default
  private Set<BoardImage> imageSet = new HashSet<>();


  // BoardImage엔티티는 별도의 JpaRepository를 생성하지 않아도 상위엔티티(Board)에서
  // 하위엔티티(BoardImage)객체를 관리하는 기능 추가해서 사용

  // Board객체에서 BoardImage객체를 관리하도록 하기 위해
  // addImage(), clearImage() 작성
  public void addImage(String uuid, String fileName){

    // 상위 엔티티에서 하위 에티티 생성
    BoardImage boardImage = BoardImage.builder()
        .uuid(uuid)
        .fileName(fileName)
        .board(this)
        .ord(imageSet.size())
        .build();

    // 첨부파일 생성하여 Set추가
    imageSet.add(boardImage);
  }

  // 삭제 처리 기능
  public void clearImage(){
    imageSet.forEach( boardImg -> boardImg.changeBoard(null));
    this.imageSet.clear(); // boardImage객체 데이터 삭제
  }
}

/*
 스프링 계층 구조
 1. 프레젠테이션 계층(컨트롤러)
  - HTTP요청을 받고 이 요청을 비즈니스 계층으로 전송하는 역할
 2. 비지니스 계층(서비스)
  - 모든 비즈니스 로직 처리(서비스를 만들기 위한 로직)
 3. 퍼시스턴스 계층(리포지토리):Entity가 작업대상
  - 모든 데이터베이스 관련 로직을 처리
 4. 데이터베이스(database)
  - 엔티티와 1:1맵핑된 테이블은 실제 DB작업을 반영




 */




