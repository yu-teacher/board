package com.beasts.board.comment.service;

import com.beasts.board.comment.entity.Comment;
import com.beasts.board.common.service.PasswordProtectedService;

import java.util.List;

public interface CommentService extends PasswordProtectedService<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    Comment createReply(Comment parent, Comment reply, String password);
    List<Comment> findReplies(Long commentId);
}
