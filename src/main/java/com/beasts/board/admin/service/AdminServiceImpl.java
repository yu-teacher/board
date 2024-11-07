package com.beasts.board.admin.service;

import com.beasts.board.common.config.JwtUtil;
import com.beasts.board.admin.entity.Admin;
import com.beasts.board.comment.entity.Comment;
import com.beasts.board.post.entity.Post;
import com.beasts.board.common.exception.InvalidPasswordException;
import com.beasts.board.admin.repository.AdminRepository;
import com.beasts.board.comment.repository.CommentRepository;
import com.beasts.board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Admin login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("관리자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        return admin;
    }

    @Override
    public String createToken(Admin admin) {
        return jwtUtil.createToken(admin);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }
}
