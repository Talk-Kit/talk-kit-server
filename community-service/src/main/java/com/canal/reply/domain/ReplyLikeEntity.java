package com.canal.reply.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@Table(name = "reply_like")
public class ReplyLikeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_seq",updatable = false)
    private Long likeSeq;

    @Column(name= "reply_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "reply_seq", nullable = false)
    private Long replySeq;

}
