package com.spring.myproject.blog.dto;

import com.spring.myproject.blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
  private String title;
  private String content;

  // dto -> entity
  public Article toEntity() {
    return Article.builder()
        .title(title)
        .content(content)
        .build();
  }
}
