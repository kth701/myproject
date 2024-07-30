package com.spring.myproject.entity;


import com.spring.myproject.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Entity : 테이블 구조 설계
@Getter
@Setter
@ToString
@AllArgsConstructor@NoArgsConstructor@Builder
@Entity
@Table(name="item")
public class Item {

  @Id
  @Column(name = "item_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;    // 상품코드

  @Column(nullable = false, length = 50)
  //@Column(columnDefinition = "varchar(50) default '상품' not null ")
  private String itemNm;  //상품명

  @Column(nullable = false, name = "price")
  private int price;  // 상품가격

  @Column(nullable = false)
  private int stockNumber; // 재고수량

  @Lob
  @Column(nullable = false)
  private String itemDetail; //상품상세설명

  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus; // 상품 판매 상태

  private LocalDateTime regTime; // 등록 시간
  private LocalDateTime updateTime; // 수정 시간

}