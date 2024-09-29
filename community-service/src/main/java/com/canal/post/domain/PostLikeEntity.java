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
@Table(name = "post_like")
public class PostLikeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_seq",updatable = false)
    private Long likeSeq;

    @Column(name= "post_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "post_seq", nullable = false)
    private Long postSeq;

    public void deletePostLike(){
        this.deleted = true;
        setUpdatedAt(LocalDateTime.now());
    }

}
