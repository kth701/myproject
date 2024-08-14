package com.spring.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
@Builder
@ToString(exclude = "board")
public class BoardImage implements  Comparable<BoardImage> {
  @Id
  private String uuid;

  private String fileName;  // 파일이름
  private int ord;          // 순번

  // 하나의 게시글에는 여러개의 첨부파일이 연결
  @ManyToOne(fetch = FetchType.LAZY)
  // 생략시 : @JoinColumn(name="board_bno") 형식으로 기본설정됨.
  private Board board;

  @Override
  public int compareTo(BoardImage other) {

    // 정렬항목이 숫자일 경우
    return this.ord - other.ord; // 오름차순
    //return other.ord - this.ord; // 내림차순
  }

  public void changeBoard(Board board){
    this.board = board;
  }
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