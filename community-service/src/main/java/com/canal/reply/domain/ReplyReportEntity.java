package com.canal.reply.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@Table(name = "reply_report")
public class ReplyReportEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_seq",updatable = false)
    private Long reportSeq;

    @Column(name= "reply_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

    @Column(name = "report_reason", nullable = false)
    private String reportReason;

    @Column(name = "checked", nullable = false)
    @ColumnDefault("false")
    private boolean checked;

    @Column(name = "report_user_seq", nullable = false)
    private Long reportUserSeq;

    @Column(name = "reported_user_seq", nullable = false)
    private Long reportedUserSeq;

    @Column(name = "reply_seq", nullable = false)
    private Long replySeq;
}
