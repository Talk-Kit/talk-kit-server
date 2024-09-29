package com.canal.reply.dto;

import com.canal.reply.domain.ReplyReportEntity;

public record ResponseReplyReportRecord(
        Long reportSeq,
        boolean deleted,
        String reportReason,
        boolean checked,
        Long reportUserSeq,
        Long reportedUserSeq,
        Long replySeq
){
    public ResponseReplyReportRecord(ReplyReportEntity replyReportEntity) {
        this(replyReportEntity.getReportSeq(), replyReportEntity.isDeleted(), replyReportEntity.getReportReason(),
                replyReportEntity.isChecked(), replyReportEntity.getReportUserSeq(),
                replyReportEntity.getReportedUserSeq(), replyReportEntity.getReplySeq());
    }
}
