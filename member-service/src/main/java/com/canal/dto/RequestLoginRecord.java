package com.canal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestLoginRecord(
        @NotNull
        @Size(min = 6, max = 20,message = "아이디는 6자 이상 20자 이하여야 합니다.")
        String userId,

        @NotNull
        @Size(min = 8, max = 30,message = "비밀번호는 8자 이상 30자 이하여야 합니다")
        String userPwd
) {
}
