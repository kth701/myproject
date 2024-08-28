package com.spring.myproject.blog.controller;

import com.spring.myproject.blog.domain.Article;
import com.spring.myproject.blog.dto.AddArticleRequest;
import com.spring.myproject.blog.dto.ArticleResponse;
import com.spring.myproject.blog.dto.UpdateArticleRequest;
import com.spring.myproject.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogController {
  private final BlogService blogService;

  // 1. 등록
  @PostMapping("/api/articles")
  public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
    Article savedArticle = blogService.save(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
  }

  // 2. 목록
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

  // 3. 조회
  @GetMapping("/api/articles/{id}")
  public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id){
    Article article = blogService.findById(id);
    // entity -> dto -> json형식으로 응답
    return ResponseEntity.ok().body(new ArticleResponse(article));
  }
  // 4. 삭제
  @DeleteMapping("/api/articles/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable long id){
    blogService.delete(id);
    return ResponseEntity.ok().build();
  }
  // 5. 수정
  @PutMapping("/api/articles/{id}")
  public ResponseEntity<Article> updateArticle(
                        @PathVariable long id,
                        @RequestBody UpdateArticleRequest request){
    Article updateArticle = blogService.update(id, request);
    return ResponseEntity.ok().body(updateArticle);
  }

}
