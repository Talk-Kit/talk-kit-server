package com.canal.post.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "post")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_seq",updatable = false)
    private Long postSeq;

    @Column(name = "post_title", nullable = false)
    private String postTitle;

    @Column(name = "post_type", nullable = false)
    private int postType;

    @Column(name= "post_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

    @Column(name = "post_like_num", nullable = false)
    @ColumnDefault("0")
    private int postLikeNum;

    @Column(name = "post_content", nullable = false)
    private String postContent;

    @Column(name = "post_scope", nullable = false)
    private String postScope;

    @Column(name = "user_seq", nullable = false, updatable = false)
    private Long userSeq;

    public void updatePost(String postTitle, int postType, String postContent, LocalDateTime updatedAt){
        this.postTitle = postTitle;
        this.postType = postType;
        this.postContent = postContent;
        setUpdatedAt(updatedAt);
    }

    public void deletePost(){
        this.deleted = true;
        setUpdatedAt(LocalDateTime.now());
    }

}
