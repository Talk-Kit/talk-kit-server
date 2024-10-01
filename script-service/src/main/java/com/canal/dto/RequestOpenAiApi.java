package com.canal.dto;

import java.util.List;

public record RequestOpenAiApi(String model, List<Messages> messages) {
    public record Messages(String role,String content) {}

}

