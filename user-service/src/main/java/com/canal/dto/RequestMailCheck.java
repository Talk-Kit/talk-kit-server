package com.canal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestMailCheck {
    @NotNull
    private String userEmail;
}
