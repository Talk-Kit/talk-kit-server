package com.canal.post.dto;

public record ResponseUserRecord(
        Long userSeq,
        String userId,
        String userPwd,
        String userEmail,
        String userNickname,
        String userAffiliation,
        boolean deleted,
        boolean termsOfAgreement,
        boolean personalInfoAgreement,
        boolean marketingAgreement
) {
}
