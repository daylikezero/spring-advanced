package org.example.expert.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {
    
    @NotBlank(message = "이메일은 필수값입니다.")
    @Email(message = "이메일 형식으로 작성되어야 합니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;
}
