package com.spring.myproject.entity;

import com.spring.myproject.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // "order" 단어는 db에서 이미사용되는 키워드
@Getter@Setter
public class Order extends BaseEntity {
  @Id@GeneratedValue
  @Column(name="order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")     // FK 설정(private Long member_id필드를 FK로 설정)
  private Member member;              // 1명의 회원은 여러개의 주문서를 연결

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;      // 주문상태(주문, 취소)
  private LocalDateTime orderDate;      // 주문 일자
  // 주문작성일,주문수정일 상속받음

  // 주문 상품 맵핑
  // 외래키(order_id)가 order_item테이블에 있으며,  연관 관계의 OrderItem에티티가 주체임
  // Order엔티티가 주인(주체)이 아니므로 "mappedBy"속성으로 연관 관계의 주인을 설정
  // OrderItem에 있는 Order에 의해 관리된다는 의미

  // 외래키가 아닌 엔티티를 주인(주체)로 설정 할 경우
  // 어떤 엔티티의 속성으로 매핑할 경우
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();


}


/*  다대일, 일대다 양방향 맵핑
주문서 -> 1명의 회원이 여러개의 주문서를 연결

member                        orders                    order_item
--------------------------------------------------------------------
member_id(PK)                 order_id(PK)              order_item_id(PK)
name, email,...               member_id(FK),            order_id(FK)
                              order_date,..             item_id(FK)
                                                        order_price, count,....

   orders
   -------
   order_id(PK)
   member_id(FK)
   order_date,...

    order_item_id(PK), order_id(FK),item_id(FK), order_price,...


- 연관관계의 주인은 외래키가 있는 곳으로 설정
- 연관 관계의 주인이 외래키를 관리(등록, 수정, 삭제)

- 주인이 아닌쪽은 연관 관계 매핑시 mappedBy 속성의 값으로 연관관계의 주인을 설정
- 주인이 아니 쪽은 읽기만 가능


CASCADE종류

PERSIST   부모 엔티티가 영속화 될 때 자식 엔티티도 영속화
MERGE
REMOVE    부모 엔티티가 삭제 될 때 연관된 자식 엔티티도 삭제
REFRESH
DETACH
ALL     부모 엔티티의 영속성 상태변화를 자식 엔티티에 모두 전이

- 고객이 주문할 상품을 선택하고 주문할 때 주문 엔티티를 저장하면서 주문 상품 엔티티도 함께 저장되는 경우


 */