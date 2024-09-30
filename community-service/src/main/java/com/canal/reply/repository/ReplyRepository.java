package com.canal.reply.repository;

import com.canal.reply.domain.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findByPostSeqAndParentReplySeq(Long postSeq, Long parentReplySeq);
    ReplyEntity findByReplySeq(Long replySeq);
    ReplyEntity findByReplySeqAndUserSeq(Long replySeq, Long userSeq);
    List<ReplyEntity> findByPostSeq(Long postSeq);
    List<ReplyEntity> findByPostSeqOrderByReplyOrderAscReplyDepth(Long postSeq);
}
