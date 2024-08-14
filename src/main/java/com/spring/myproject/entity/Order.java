package com.spring.myproject.entity;

import com.spring.myproject.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // "order" 단어는 db에서 이미사용되는 키워드
@Getter@Setter
public class Order extends BaseEntity {
  @Id@GeneratedValue
  @Column(name="order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")     // FK 설정
  private Member member;              // 1명의 회원은 여러개의 주문서를 연결

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;      // 주문상태(주문, 취소)
  private LocalDateTime orderDate;      // 주문 일자
  // 주문작성일,주문수정일 상속받음

  // 주문 상품 맵핑
//  private List<OrderItem> orderItems = new ArrayList();






}


/*  다대일, 일대다 양방향 맵핑
주문서 -> 1명의 회원이 여러개의 주문서를 연결

member                        orders                    order_item
--------------------------------------------------------------------
member_id(PK)                 order_id(PK)              order_item_id(PK)
name, email,...               member_id(FK),            order_id(FK)
                              order_date,..             item_id(FK)
                                                        order_price, count,....

- 연관관계의 주인은 외래키가 있는 곳으로 설정
- 연관 관계의 주인이 외래키를 관리(등록, 수정, 삭제)
- 주인이 아닌쪽은 연관 관계 매핑시 mappedBy 속성의 값으로 연관관계의 주인을 설정
- 주인이 아니 쪽은 읽기만 가능






 */