package com.canal.reply.repository;

import com.canal.reply.domain.ReplyLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyLikeRepository extends JpaRepository<ReplyLikeEntity, Long> {
    List<ReplyLikeEntity> findByReplySeq(Long replySeq);
    ReplyLikeEntity findByReplySeqAndUserSeq(Long replySeq, Long userSeq);
    ReplyLikeEntity findByLikeSeqAndUserSeq(Long likeSeq, Long userSeq);
}
