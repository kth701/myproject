package com.spring.myproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.myproject.entity.Board;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
  private Long rno;

  // spring-boot-starter-validation: 서버 유효성 검사 라이브러리
  @NotNull
  private Long bno;       // 댓글의 부모: 클라이언트로 부터 넘어온 게시글 번호
  //private Board board;    // replyDTO와 reply Entity 맵핑을 위한 객체

  @NotEmpty
  private String replyText;
  @NotEmpty
  private String replyer;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime regDate;
  @JsonIgnore
  private LocalDateTime  modDate;
}

/*
@JsonIgnore, @JsonIgnoreProperties, @JsonIgnoreType 이러한 주석은 JSON 직렬화, 역직렬화에서
속성을 무시하는데 사용됩니다.

@JsonIgnore 어노테이션은 클래스의 속성(필드, 멤버변수) 수준에서 사용되고
@JsonIgnoreProperties 어노테이션은 클래스 수준(클래스 선언 바로 위에)에 사용됩니다.
@JsonIgnoreType 어노테이션은 클래스 수준에서 사용되며 전체 클래스를 무시합니다.

 */