package com.beasts.board.post.service;

import com.beasts.board.common.service.AbstractPasswordProtectedService;
import com.beasts.board.post.entity.Post;
import com.beasts.board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl extends AbstractPasswordProtectedService<Post, Long>
        implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PasswordEncoder passwordEncoder, PostRepository postRepository) {
        super(passwordEncoder);
        this.postRepository = postRepository;
    }


    @Override
    @Transactional
    public Post create(Post entity) {
        return postRepository.save(entity);
    }

    @Override
    @Transactional
    public Post createWithPassword(Post entity, String rawPassword) {
        return create(Post.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .nickname(entity.getNickname())
                .password(encodePassword(rawPassword))
                .build());
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Page<Post> findAllWithPaging(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    @Transactional
    public Post update(Post entity) {
        Post post = findById(entity.getId());
        post.update(entity.getTitle(), entity.getContent());
        return post;
    }

    @Override
    @Transactional
    public Post updateWithPassword(Post entity, String password) {
        Post post = findById(entity.getId());
        validatePassword(password, post.getPassword());
        return update(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteWithPassword(Long id, String password) {
        Post post = findById(id);
        validatePassword(password, post.getPassword());
        delete(id);
    }

    @Override
    public Long getId(Post entity) {
        return entity.getId();
    }

    @Override
    public Page<Post> search(String keyword, String searchType, Pageable pageable) {
        if ("title".equals(searchType)) {
            return postRepository.searchByTitle(keyword, pageable);
        } else if ("content".equals(searchType)) {
            return postRepository.searchByContent(keyword, pageable);
        } else if ("nickname".equals(searchType)) {
            return postRepository.searchByNickname(keyword, pageable);
        }
        return postRepository.search(keyword, pageable);  // 전체 검색
    }

    @Override
    @Transactional
    public void increaseViewCount(Long id) {
        Post post = findById(id);
        post.increaseViewCount();
    }
}
