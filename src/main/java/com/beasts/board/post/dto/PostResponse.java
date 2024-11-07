package com.beasts.board.post.dto;

import com.beasts.board.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String nickname;
    private final int viewCount;
    private final LocalDateTime createdAt;
    private final int commentCount;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getNickname();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
        this.commentCount = post.getComments().size();
    }
}
