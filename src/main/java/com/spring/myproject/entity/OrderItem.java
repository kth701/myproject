package com.spring.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class OrderItem extends BaseEntity{   // 주문작성일,주문수정일 상속받음
  @Id@GeneratedValue
  @Column(name="order_item_id")
  private Long id;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="item_id")
  private Item item;        // 하나의 상품은 여러 주문상품을 연결

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="order_id")
  private Order order;        // 한번의 주문은 여러개의 상품 연결

}


/*  다대일, 일대다 양방향 맵핑
주문서 -> 1명의 회원이 여러개의 주문서를 연결

orders                    order_item                item
----------------------------------------------------------------------------------------------
order_id(PK)              order_item_id(PK)         item_id(PK)
member_id(FK),            order_id(FK)              item_name,...
                          item_id(FK)               order_price, count,....
                          order_date,..

// 주문A-> 상품1,상품2(1:N) ,   상품1-> A주문,B주문 (1:M)
Order_item_id       order_id        item_id
1                   A               상품1
2                   A               상품2
3                   B               상품1
4                   B               상품2
5                   B               상품3



 */
