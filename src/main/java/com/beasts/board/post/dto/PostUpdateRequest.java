package com.beasts.board.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {
    private Long id;
    private String title;
    private String content;
    private String password;
}
