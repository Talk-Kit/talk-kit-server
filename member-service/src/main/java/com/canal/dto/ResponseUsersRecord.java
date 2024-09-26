package com.canal.dto;

import com.canal.domain.UserEntity;

public record ResponseUsersRecord(
    Long userSeq,
    String userId,
    String encryptPwd,
    String userEmail,
    String userNickname,
    String userAffiliation
){
    public ResponseUsersRecord(UserEntity userEntity) {
        this(userEntity.getUserSeq(),userEntity.getUserId(), userEntity.getUserPwd(), userEntity.getUserEmail(),
                userEntity.getUserNickname(), userEntity.getUserAffiliation());
    }
}
