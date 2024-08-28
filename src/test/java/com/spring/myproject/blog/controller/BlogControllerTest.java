package com.spring.myproject.blog.controller;

import com.google.gson.Gson;
import com.spring.myproject.blog.domain.Article;
import com.spring.myproject.blog.dto.AddArticleRequest;
import com.spring.myproject.blog.dto.UpdateArticleRequest;
import com.spring.myproject.blog.repository.BlogRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// 테스트용 애플리케이션 컨텍스트 생성
@SpringBootTest
@Log4j2
@AutoConfigureMockMvc // 테스트용 MVC환경에서 요청 및 전송,응답 기능을 제공, 컨트롤러 테스트 할 때 사용
//@TestPropertySource(locations = {"classpath:application-test.properties"})
class BlogControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;

  @Autowired
  private BlogRepository blogRepository;
  @Autowired
  private Gson gson;


  // 테스트 케이스 실행 전에 수행하는 메서드
  @BeforeEach
  public void mockMvcSetup(){
    this.mockMvc =
        MockMvcBuilders
            .webAppContextSetup(context).build();

    blogRepository.deleteAll();
  }

  @DisplayName("addArticle: 블로그 글 추가 테스트")
  @Test
  void addArticle() throws Exception{
    // given
    final String url      ="/api/articles";
    final String title    = "title";
    final String content  = "content";
    // dto 객체 생성
    final AddArticleRequest userRequest = new AddArticleRequest(title, content);

    // java객체 DTO -> JSON구조형식을 가진 문자열로 전환
    final String requestBody = gson.toJson(userRequest);

    // when
    ResultActions result = mockMvc.perform(MockMvcRequestBuilders
        .post(url)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestBody))
        .andDo(print());
    // then
    result.andExpect(status().isCreated());

    List<Article> articles = blogRepository.findAll();

    // DB반영 전  data와 DB 반영 후 data
    assertThat(articles.size()).isEqualTo(1);
    assertThat(articles.get(0).getTitle()).isEqualTo(title);
    assertThat(articles.get(0).getContent()).isEqualTo(content);
  }

  @DisplayName("findAllArticles: 블로그 글 목록 조회 테스트")
  @Test
  void findAllArticles() throws Exception{
    final String url = "/api/articles";
    final String title = "title";
    final String content = "content";

    blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .build());

    // then
    final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
        .get(url)
        .accept(MediaType.APPLICATION_JSON));
    // then
    resultActions.andExpect(status().isOk())
        .andExpect(jsonPath("$[0].content").value(content))
        .andExpect(jsonPath("$[0].title").value(title));

  }

  @DisplayName("findArticle: 블로그 글 조회 테스트")
  @Test
  public void findArticle() throws Exception{
    // given
    final String url = "/api/articles/{id}";
    final String title = "title";
    final String content = "content";

    // 테스트용 entity 생성하여 db에 저장
    Article savedArticle = blogRepository.save(
        Article.builder()
            .title(title)
            .content(content)
        .build());

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(url, savedArticle.getId()));
    // then
    resultActions.andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value(content))
        .andExpect(jsonPath("$.title").value(title)).andDo(print());
  }

  @DisplayName("deleteArticle: 블로그 글 삭제 테스트")
  @Test
  public void deleteArticle() throws Exception{
    // given
    final String url ="/api/articles/{id}";
    final String title = "title";
    final String content = "content";

    // 테스트용 entity 생성하여 db에 저장
    Article savedArticle = blogRepository.save(
        Article.builder()
            .title(title)
            .content(content)
            .build());

    log.info("\n=> delete Article: "+savedArticle);
    // when
    mockMvc.perform(  MockMvcRequestBuilders.delete(url, savedArticle.getId())  )
          .andExpect(status().isOk())
          .andDo(print());
    // then
    List<Article> articles = blogRepository.findAll();

    assertThat(articles).isEmpty();
  }

  @DisplayName("updateArticle: 블로그 글 수정 테스트")
  @Test
  public void updateArticle() throws Exception{
     // given
    final String url ="/api/articles/{id}";
    final String title = "title";
    final String content = "content";

    // 테스트용 entity 생성하여 db에 저장
    Article savedArticle = blogRepository.save(
        Article.builder()
            .title(title)
            .content(content)
            .build());

    final String newTitle = "new Title";
    final String newContent = "new Content";

    UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);
    String updateData = gson.toJson(request);

    // when
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(url, savedArticle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .content(updateData)
            .contentType(MediaType.APPLICATION_JSON_VALUE))  ;

    // then
    resultActions.andExpect(status().isOk()).andDo(print());

    Article article = blogRepository.findById(savedArticle.getId()).get();


    assertThat(article.getTitle()).isEqualTo(newTitle);
    assertThat(article.getContent()).isEqualTo(newContent);

  }

}