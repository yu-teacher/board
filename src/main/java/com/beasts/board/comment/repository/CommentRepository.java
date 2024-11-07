package com.beasts.board.comment.repository;

import com.beasts.board.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtDesc(Long postId);
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
    void deleteByPostId(Long postId);
}