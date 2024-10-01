package com.canal.dto;

import com.canal.domain.ProjectEntity;


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
