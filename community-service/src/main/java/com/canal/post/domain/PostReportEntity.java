package com.canal.post.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "post_report")
public class PostReportEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_seq",updatable = false)
    private Long reportSeq;

    @Column(name= "post_deleted", nullable = false)
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

    @Column(name = "post_seq", nullable = false)
    private Long postSeq;

    public void createPostReport(Long reportUserSeq, Long reportedUserSeq, Long postSeq){
        this.reportUserSeq = reportUserSeq;
        this.reportedUserSeq = reportedUserSeq;
        this.postSeq = postSeq;
    }

    public void updatePostReport(String reportReason){
        this.reportReason = reportReason;
        setUpdatedAt(LocalDateTime.now());
    }

    public void deletePostReport(){
        this.deleted = true;
        setUpdatedAt(LocalDateTime.now());
    }
}
