package com.canal.dto;

import com.canal.domain.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "커뮤니티 사용자 응답 DTO")
public record ResponseUserForComm(
        Long userSeq,
    String userNickname,
    String userAffiliation
){
    public ResponseUserForComm(UserEntity userEntity) {
        this(userEntity.getUserSeq(),userEntity.getUserNickname(), userEntity.getUserAffiliation());
    }
}
