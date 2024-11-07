package com.beasts.board.comment.service;

import com.beasts.board.comment.entity.Comment;
import com.beasts.board.common.service.AbstractPasswordProtectedService;
import com.beasts.board.post.entity.Post;
import com.beasts.board.comment.repository.CommentRepository;
import com.beasts.board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl extends AbstractPasswordProtectedService<Comment, Long>
        implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(
            PasswordEncoder passwordEncoder,
            CommentRepository commentRepository,
            PostRepository postRepository) {
        super(passwordEncoder);
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public Comment create(Comment entity) {
        Post post = postRepository.findById(entity.getPost().getId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(entity.getContent())
                .nickname(entity.getNickname())
                .password(entity.getPassword())
                .post(post)
                .parent(entity.getParent())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment createWithPassword(Comment entity, String rawPassword) {
        return create(Comment.builder()
                .content(entity.getContent())
                .nickname(entity.getNickname())
                .password(encodePassword(rawPassword))
                .post(entity.getPost())
                .build());
    }

    @Override
    @Transactional
    public Comment createReply(Comment parent, Comment reply, String password) {
        Comment parentComment = findById(parent.getId());
        return create(Comment.builder()
                .content(reply.getContent())
                .nickname(reply.getNickname())
                .password(encodePassword(password))
                .post(parentComment.getPost())
                .parent(parentComment)
                .build());
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtDesc(postId);
    }

    @Override
    public List<Comment> findReplies(Long commentId) {
        return commentRepository.findByParentIdOrderByCreatedAtAsc(commentId);
    }

    @Override
    @Transactional
    public Comment update(Comment entity) {
        Comment comment = findById(entity.getId());
        comment.update(entity.getContent());
        return comment;
    }

    @Override
    @Transactional
    public Comment updateWithPassword(Comment entity, String password) {
        Comment comment = findById(entity.getId());
        validatePassword(password, comment.getPassword());
        return update(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteWithPassword(Long id, String password) {
        Comment comment = findById(id);
        validatePassword(password, comment.getPassword());
        delete(id);
    }

    @Override
    public Long getId(Comment entity) {
        return entity.getId();
    }
}
