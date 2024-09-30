package com.canal.reply.dto;

import com.canal.reply.domain.ReplyLikeEntity;

public record ResponseReplyLikeRecord(
        Long likeSeq,
        boolean deleted,
        Long userSeq,
        Long replySeq
){
    public ResponseReplyLikeRecord(ReplyLikeEntity replyLikeEntity) {
        this(replyLikeEntity.getLikeSeq(), replyLikeEntity.isDeleted(),
                replyLikeEntity.getUserSeq(), replyLikeEntity.getReplySeq());
    }
}
