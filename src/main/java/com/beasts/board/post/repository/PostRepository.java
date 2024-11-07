package com.beasts.board.post.repository;

import com.beasts.board.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% " +
            "ORDER BY p.createdAt DESC")
    Page<Post> search(@Param("keyword") String keyword, Pageable pageable);

    // 개별 검색 메서드들 추가
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> searchByContent(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.nickname LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> searchByNickname(@Param("keyword") String keyword, Pageable pageable);
}
