package com.beasts.board.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDTO {
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    private Long postId;
    private Long parentId;  // 대댓글인 경우
}
