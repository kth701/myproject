package com.spring.myproject.blog.dto;

import com.spring.myproject.blog.domain.Article;
import lombok.*;


@Getter@Setter
public class ArticleResponse {
  private final String title;
  private final String content;

  // entity -> dto
  public ArticleResponse(Article article){
    this.title = article.getTitle();
    this.content = article.getContent();
  }
}
