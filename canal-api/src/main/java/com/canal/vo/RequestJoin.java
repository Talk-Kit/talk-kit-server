package com.canal.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestJoin {
    @NotNull
    @Size(min = 6, max = 20,message = "아이디는 6자 이상 20자 이하여야 합니다.")
    private String userId;

    @NotNull
    @Size(min = 8, max = 30,message = "비밀번호는 8자 이상 30자 이하여야 합니다")
    private String userPwd;

    @NotNull
    private String userEmail;

    @NotNull
    @Size(min = 2, max = 12,message = "닉네임은 2자 이상 12자 이하여야 합니다.")
    private String userNickname;

    @NotNull(message = "소속은 null일 수 없습니다.")
    private String userAffiliation;
}
