package com.canal.dto;

import com.canal.domain.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자정보 응답 DTO")
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
