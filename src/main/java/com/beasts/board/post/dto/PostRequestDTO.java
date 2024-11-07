package com.beasts.board.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDTO {
    @NotBlank(message = "제목을 입력해주세요")
    @Size(min = 2, max = 100, message = "제목은 2-100자 사이여야 합니다")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 20, message = "닉네임은 2-20자 사이여야 합니다")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 4, max = 20, message = "비밀번호는 4-20자 사이여야 합니다")
    private String password;
}
