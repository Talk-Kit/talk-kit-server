package com.canal.post.repository;

import com.canal.post.domain.PostReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostReportRepository extends JpaRepository<PostReportEntity, Long> {
    PostReportEntity findByReportSeq(Long reportSeq);
    List<PostReportEntity> findByPostSeq(Long postSeq);
    PostReportEntity findByReportSeqAndReportUserSeq(Long reportSeq, Long reportUserSeq);
}
