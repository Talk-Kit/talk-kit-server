package com.canal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "회원가입 요청 DTO")
public class RequestJoin {
    @Schema(description = "ID")
    @NotNull
    @Size(min = 6, max = 20,message = "아이디는 6자 이상 20자 이하여야 합니다.")
    private String userId;

    @Schema(description = "Password")
    @NotNull
    @Size(min = 8, max = 30,message = "비밀번호는 8자 이상 30자 이하여야 합니다")
    private String userPwd;

    @Schema(description = "Email")
    @NotNull
    private String userEmail;

    @Schema(description = "Nickname")
    @NotNull
    @Size(min = 2, max = 12,message = "닉네임은 2자 이상 12자 이하여야 합니다.")
    private String userNickname;

    @Schema(description = "소속",allowableValues = {"청소년", "대학생","직장인","기타"})
    @NotNull(message = "소속은 null일 수 없습니다.")
    private String userAffiliation;

    @Schema(description = "이용약관 동의여부",defaultValue = "false")
    @NotNull
    private boolean termsOfAgreement;

    @Schema(description = "개인정보수집 동의여부",defaultValue = "false")
    @NotNull
    private boolean personalInfoAgreement;

    @Schema(description = "마켓팅수신 동의여부",defaultValue = "false")
    @NotNull
    private boolean marketingAgreement;
}
