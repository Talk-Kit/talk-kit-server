package com.canal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "작성한 대본을 저장하기 위한 DTO")
public record RequestScript(
        @Schema(description="파일명(대본명)")
        String fileName,
        @Schema(description="파일내용(대본내용)")
        String fileContent
) {
}
