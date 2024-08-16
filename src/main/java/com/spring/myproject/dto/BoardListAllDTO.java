package com.spring.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListAllDTO {
  private Long bno;
  private String title;
  private String write;
  private String email;
  private LocalDateTime regDate;
  private Long replyCount;

  private List<BoardImageDTO> boardImages;
}
