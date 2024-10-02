package com.canal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대본 저장시 새 프로젝트를 생성하기 위한 DTO")
public record RequestNewProject(
        @Schema(description = "생성할 프로젝트명")
        String projectName,
        @Schema(description = "사용자 ID")
        String userId
) {
}
