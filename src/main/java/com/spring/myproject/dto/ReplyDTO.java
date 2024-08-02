package com.spring.myproject.dto;

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
  private Long bno;// 댓글의 부모
  //private Board board;

  @NotEmpty
  private String replyText;
  @NotEmpty
  private String replyer;

  private LocalDateTime regDate, modDate;
}
