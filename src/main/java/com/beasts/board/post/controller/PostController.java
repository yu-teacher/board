package com.beasts.board.post.controller;

import com.beasts.board.common.dto.PasswordRequest;
import com.beasts.board.post.dto.PostCreateRequest;
import com.beasts.board.post.dto.PostUpdateRequest;
import com.beasts.board.post.entity.Post;
import com.beasts.board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody @Valid PostCreateRequest request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .nickname(request.getNickname())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createWithPassword(post, request.getPassword()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        postService.increaseViewCount(id);
        return ResponseEntity.ok(postService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.Direction.valueOf(direction),
                sort
        );
        return ResponseEntity.ok(postService.findAllWithPaging(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostUpdateRequest request) {
        Post post = postService.findById(id);
        post.update(request.getTitle(), request.getContent());
        return ResponseEntity.ok(postService.updateWithPassword(post, request.getPassword()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @RequestBody @Valid PasswordRequest request) {
        postService.deleteWithPassword(id, request.getPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "title") String searchType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.Direction.valueOf(direction),
                sort
        );
        return ResponseEntity.ok(postService.search(keyword, searchType, pageable));
    }
}
