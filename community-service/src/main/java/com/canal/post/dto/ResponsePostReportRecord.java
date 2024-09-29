package com.canal.post.dto;

import com.canal.post.domain.PostReportEntity;

public record ResponsePostReportRecord(
        Long reportSeq,
        boolean deleted,
        String reportReason,
        boolean checked,
        Long reportUserSeq,
        Long reportedUserSeq,
        Long postSeq
){
    public ResponsePostReportRecord(PostReportEntity postReportEntity) {
        this(postReportEntity.getReportSeq(), postReportEntity.isDeleted(), postReportEntity.getReportReason(),
                postReportEntity.isChecked(), postReportEntity.getReportUserSeq(),
                postReportEntity.getReportedUserSeq(), postReportEntity.getPostSeq());
    }
}
