package com.canal.dto;

import lombok.Data;

@Data
public class UserDto {
    private String userId;
    private String userPwd;
    private String userEmail;
    private String userNickname;
    private String userAffiliation;
}
