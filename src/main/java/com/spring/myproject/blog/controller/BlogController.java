package com.spring.myproject.blog.controller;

import com.spring.myproject.blog.domain.Article;
import com.spring.myproject.blog.dto.AddArticleRequest;
import com.spring.myproject.blog.dto.ArticleResponse;
import com.spring.myproject.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogController {
  private final BlogService blogService;

  @PostMapping("/api/articles")
  public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
    Article savedArticle = blogService.save(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
  }

  @GetMapping("/api/articles")
  public ResponseEntity<List<ArticleResponse>> findAllArticles(){
    List<ArticleResponse> articles =
        blogService.finall()
            .stream()
            //.map((article) -> modelMapper.map(article, ArticleResponse.class)
            .map(ArticleResponse::new)      // entity -> dto
            .toList();  // stream(dto) -> list(dto)

    return ResponseEntity.ok().body(articles);
  }
}
