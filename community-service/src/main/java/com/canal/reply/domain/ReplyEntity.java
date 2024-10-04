package com.canal.reply.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@Table(name = "reply")
public class ReplyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_seq",updatable = false)
    private Long replySeq;

    @Column(name= "reply_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

    @Column(name = "reply_content", nullable = false)
    private String replyContent;

    @Column(name = "reply_depth", nullable = false)
    private int replyDepth;

    @Column(name = "reply_order", nullable = false)
    private int replyOrder;

    @Column(name = "reply_like_num")
    private int replyLikeNum;

    @Column(name = "post_seq", nullable = false, updatable = false)
    private Long postSeq;

    @Column(name = "user_seq", nullable = false, updatable = false)
    private Long userSeq;

    @Column(name = "parent_reply_seq", updatable = false)
    private Long parentReplySeq;

}
