package com.canal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestMailCodeCheck {
    @NotNull
    private String userEmail;

    @NotNull
    private String authCode;
}