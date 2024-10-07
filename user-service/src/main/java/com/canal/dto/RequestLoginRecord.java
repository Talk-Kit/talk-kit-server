package com.canal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "로그인 요청 DTO")
public record RequestLoginRecord(
        @Schema(description = "사용자 ID")
        @NotNull
        @Size(min = 6, max = 20,message = "아이디는 6자 이상 20자 이하여야 합니다.")
        String userId,

        @Schema(description = "사용자 비밀번호")
        @NotNull
        @Size(min = 8, max = 30,message = "비밀번호는 8자 이상 30자 이하여야 합니다")
        String userPwd
) {
}
