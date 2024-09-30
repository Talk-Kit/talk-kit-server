package com.canal.reply.repository;

import com.canal.reply.domain.ReplyReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyReportRepository extends JpaRepository<ReplyReportEntity, Long> {
    List<ReplyReportEntity> findByReplySeq(Long reportSeq);
    ReplyReportEntity findByReportSeqAndReportUserSeq(Long reportSeq, Long reportUserSeq);
}
