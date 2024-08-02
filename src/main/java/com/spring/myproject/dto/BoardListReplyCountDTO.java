package com.spring.myproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {

  private Long bno;
  private String title;
  private String writer;
  private LocalDateTime regDate;

  // 특정 게시글에 대한 댓글 개수
  private Long replyCount;
}
