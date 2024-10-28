package com.canal.dto;

import com.canal.domain.ProjectEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로젝트 데이터 dto")
public record ResponseProjectsRecord(
        Long projectSeq,
        String projectName,
        Long userSeq,
        String updatedAt
) {
    public ResponseProjectsRecord(ProjectEntity projectEntity) {
        this(
                projectEntity.getProjectSeq(),
                projectEntity.getProjectName(),
                projectEntity.getUserSeq(),
                projectEntity.getUpdatedAt().toString());
    }
}
