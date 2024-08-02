package com.spring.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
// 쿼리 조건으로 자주 사용되는 칼럼에는 인덱스 생성하여 사용
@Table( name="Reply",
        indexes =
            {@Index(name="idx_reply_board_bno", columnList = "board_bno")}
      )
@Getter
@Builder
@AllArgsConstructor@NoArgsConstructor
@ToString(exclude = "board") // Board Entity에 존재햐는 toString()는 작동 중지
public class Reply extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rno;

  // 현재 댓글의 필드명은 엔티티_PK으로 자동으로 생성 => board_bno
  @ManyToOne(fetch = FetchType.LAZY)  // board entity연결은 즉시 연결이 아닌 필요시에만 연결\
  private Board board; // 멤버변수 이름 => board_bno
  // 컴파일 후
  // private Long  board_bno; 형식으로 인식

  private String replyText;
  private String replyer;

}

/*
연관관계 : JPA 연관 관계 판단 기준
- 연관 관계의 기준은 항상 변화가 많은 쪽을 기준으로 결정
- ERD의 FK를 기준으로 결정

Bord Entity   <-->   Reply Entity  => 1:n
게시글           댓글

글번호 PK        글번호 PK
작성자           부모글번호 FK
글제목           댓글 내용
글내용           댓글 작성자
작성일자          댓글 작성일자
수정일자



 */
