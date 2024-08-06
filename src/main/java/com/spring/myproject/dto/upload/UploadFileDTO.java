package com.spring.myproject.dto.upload;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO {
  //  첨부파일 받기위해  MultipartFile타입의 List객체 선언
  private List<MultipartFile> files;
}
