package com.canal.reply.dto;

import com.canal.reply.domain.ReplyEntity;

public record ResponseReplyRecord(
        Long replySeq,
        boolean deleted,
        String replyContent,
        int replyDepth,
        int replyOrder,
        int replyLikeNum,
        Long postSeq,
        Long userSeq,
        Long parentReplySeq
){
    public ResponseReplyRecord(ReplyEntity replyEntity) {
        this(replyEntity.getReplySeq(), replyEntity.isDeleted(), replyEntity.getReplyContent(),
                replyEntity.getReplyDepth(), replyEntity.getReplyOrder(), replyEntity.getReplyLikeNum(),
                replyEntity.getPostSeq(), replyEntity.getUserSeq(), replyEntity.getParentReplySeq());
    }
}
