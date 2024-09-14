package com.canal.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
    private Long userSeq;
    private String userId;
    private String userEmail;
    private String userNickName;
    private String userAffiliation;
}
