package com.spring.myproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// AuditingEntityListener : 엔티티가 데이터베이스에 추가되거나 변경될 때 자동으로 시간 설정

@MappedSuperclass  // 공동속성처리
@EntityListeners(value={AuditingEntityListener.class})
@Setter@Getter
public abstract class BaseEntity {  // 공동 멤버 변수들은 추상 클래스 정의

  // 엔티티가 생성되어 저장될 때 시간을 자동으로 저장
  @CreatedDate
  @Column(name="regdate", updatable=false)  // updatable=false 수정되는 시점에 기능 off
  private LocalDateTime regDate;

  //엔티티가 값을 변경될 때 시간을 자동으로 저장
  @LastModifiedDate
  @Column(name="moddate")
  private LocalDateTime modDate;

}