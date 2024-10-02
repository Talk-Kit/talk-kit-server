package com.canal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "gpt에 초안을 요청하기 위한 DTO")
public record RequestContent(String content){
}
