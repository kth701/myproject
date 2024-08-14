package com.spring.myproject.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_item_id")
  private Long id;

  // 연관관계
  // 하나의 장바구니에 여러개의 상품 연결
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="cart_id")
  private Cart cart;
  // 하나의 상품은 여러 장바구니와 연결
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="item_id")
  private Item item;

  // 같은 상품을 장바구니에 몇 개 담을지 저장하하는 항목
  private int count;

}








/*  다대일 단방향 맵핑
하나의 장바구니에는 여러개의 상품 연결
하나의 상품은 여러장바구에 연결

장바구니          장바구니상품          상품
cart      1:N     cart_item     M:1  item
--------------------------------------------
cart_id(PK)     cart_item_id(PK)    item_id(PK)
member_id(FK)   cart_id(FK)         item_nm, price,....
                item_id(FK)
                count

cart_item
id    cart_id     item_id
1     A           A1
2     A           A2
3     A           A3
4     B           A1
5     C           B2
6     C           B3
...

 */