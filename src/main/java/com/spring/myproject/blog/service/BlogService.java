package com.spring.myproject.blog.service;

import com.spring.myproject.blog.domain.Article;
import com.spring.myproject.blog.dto.AddArticleRequest;
import com.spring.myproject.blog.dto.UpdateArticleRequest;
import com.spring.myproject.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
  private  final BlogRepository blogRepository;

  // 1. 등록
  public Article save(AddArticleRequest request){
     // dto -> entity -> save
    return blogRepository.save(request.toEntity());
  }
  // 2. 목록
  public List<Article> finall(){
    // entity -> 전체 읽기
    return blogRepository.findAll();
  }
  // 3. 조회
  public Article findById(long id){
    return blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found: "+id));
  }
  // 4. 삭제
  public void delete(long id){
    blogRepository.deleteById(id);
  }
  // 5. 수정
  @Transactional
  public Article update(long id, UpdateArticleRequest request){
    // 수정할 data 불러오기
    Article article = blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found: "+id));
    // data 변경
    article.update(request.getTitle(), request.getContent());
    // 변경된 Entity 반환
    return article;
  }


}
