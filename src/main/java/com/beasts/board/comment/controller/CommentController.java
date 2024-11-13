package com.beasts.board.comment.controller;

import com.beasts.board.comment.dto.CommentRequestDTO;
import com.beasts.board.comment.dto.CommentUpdateRequest;
import com.beasts.board.comment.entity.Comment;
import com.beasts.board.comment.service.CommentService;
import com.beasts.board.common.dto.PasswordRequest;
import com.beasts.board.post.entity.Post;
import com.beasts.board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentRequestDTO request) {
        Post post = postService.findById(postId);
        Comment comment = Comment.builder()
                .content(request.getContent())
                .nickname(request.getNickname())
                .post(post)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createWithPassword(comment, request.getPassword()));
    }

    @PostMapping("/{commentId}/replies")
    public ResponseEntity<Comment> createReply(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequestDTO request) {
        Post post = postService.findById(postId);
        Comment parentComment = commentService.findById(commentId);
        Comment reply = Comment.builder()
                .content(request.getContent())
                .nickname(request.getNickname())
                .post(post)
                .parent(parentComment)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createWithPassword(reply, request.getPassword()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long id,
            @RequestBody @Valid CommentUpdateRequest request) {
        Comment comment = commentService.findById(id);
        comment.update(request.getContent());
        return ResponseEntity.ok(
                commentService.updateWithPassword(comment, request.getPassword())
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long id,
            @RequestBody @Valid PasswordRequest request) {
        commentService.deleteWithPassword(id, request.getPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<Comment>> getReplies(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.findReplies(commentId));
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findByPostId(postId));
    }
}
