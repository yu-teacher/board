package com.beasts.board.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordRequest {
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
