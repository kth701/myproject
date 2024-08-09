package com.spring.myproject.dto;

import com.spring.myproject.constant.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor
@Builder
public class MemberDTO {

  @NotBlank(message = "이름은 필수 입력 값입니다.")  // NULL체크 및 문자열의 경우 길이 0 인지 및 빈문자열(" ") 검사
  private String name;
  @NotEmpty(message = "이메일은 필수 입력 값입니다.") // NULL체크 및 문자열의 경우 길이 0 인지 검사
  @Email(message = "이메일 형식으로 입력해주세요.")
  private String email;
  @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
  @Length(min=4, max=16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
  private String password;
  @NotEmpty(message = "주소는 필수 입력 값입니다.")
  private String address;

  // Role data 임시 저장 용
  private Role role;

}
