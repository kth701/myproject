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
