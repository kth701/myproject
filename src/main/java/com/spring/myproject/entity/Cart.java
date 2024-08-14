package com.spring.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="cart")
@Getter@Setter@ToString
public class Cart extends BaseEntity { // 공통 멤버변수 상속
  @Id
  @Column(name="cart_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  /*
  Entity에서 특정 Entity참조할 경우 반드시 연관관계 어노테이션을 적용
  - 장바구니가 엔티티가 회원엔티티를 참조하는 형태: 장바구니(외래키), 회원(참조:기본키)
  - FetchType.EAGER(즉시조인, 기본값으로 설정되어 있음), FetchType.LAZY(필요할 때 조인)

   */

  // member_id필드는 FK키를 설정
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="member_id")       // 맵핑할 외래키(FK)설정
  private Member member;              // private Long member_id 인식


  // 회원 장바구니 생성하는 메서드
  // 회원1명당 1개의 장바구니 맵핑
  public static Cart createCart(Member member){

    Cart cart = new Cart();
    cart.setMember(member);

    return cart;
  }


}


/*
@OneToOne
Cart            <-->     Member
---------------------------
cart_id(PK)     1:1     member_id(PK)
member_id(FK)           name, email, password, addres, role,...

 */