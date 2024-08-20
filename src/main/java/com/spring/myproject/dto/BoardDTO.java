package com.spring.myproject.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor@NoArgsConstructor
public class BoardDTO {
  private Long bno;

  @NotEmpty
  @Size(min = 3, max=100)
  private String title;
  @NotEmpty
  private String content;
  @NotEmpty
  private String writer;

  // 현재 로그인 사용자 이메일와 게시글 작성자 이메일 동일한지 판별하기위한 항목
  private String email;

  private LocalDateTime regDate;
  private LocalDateTime modDate;

  // 첨부파일 이름: html에서 첨부파일 UI <input type="file"../> 배열구조형식
  private List<String> fileNames;// 1개이상 파일이름 저장
}
