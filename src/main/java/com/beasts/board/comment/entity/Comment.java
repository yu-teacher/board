package com.beasts.board.comment.entity;

import com.beasts.board.common.entity.PasswordProtectedEntity;
import com.beasts.board.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment extends PasswordProtectedEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String nickname;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Comment> replies = new ArrayList<>();

    private int depth;

    public void setPost(Post post) {
        if (this.post != null) {  // 기존 관계 제거
            this.post.getComments().remove(this);
        }
        this.post = post;
        if (post != null) {  // 새로운 관계 설정
            post.getComments().add(this);
        }
    }

    @Builder
    public Comment(String content, String nickname, String password, Post post, Comment parent) {
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.depth = (parent != null) ? parent.getDepth() + 1 : 0;
        this.parent = parent;
        if (post != null) {
            setPost(post);
        }
    }

    public void update(String content) {
        this.content = content;
    }
}
