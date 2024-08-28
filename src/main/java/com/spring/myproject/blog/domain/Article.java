package com.spring.myproject.blog.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값변경 목적으로 접근하는 메시지 차단
@AllArgsConstructor
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name ="blog_id", updatable = false)
  private Long id;

  @Column(name="title", nullable = false)
  private String title;
  @Column(name="content", nullable = false)
  private String content;

  @Builder
  public Article(String title, String content){
    this.title = title;
    this.content = content;
  }

  public void update(String title, String content){
    this.title = title;
    this.content = content;
  }

}
