package com.beasts.board.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequestDTO {
    @NotBlank(message = "아이디를 입력해주세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
