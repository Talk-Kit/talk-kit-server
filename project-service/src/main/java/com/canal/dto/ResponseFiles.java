package com.canal.dto;


import com.canal.domain.FileEntity;

import java.time.LocalDateTime;

public record ResponseFiles(
        Long fileSeq,
        String fileName,
        String fileContent,
        String fileUrl,
        Long projectSeq,
        LocalDateTime createdAt
) {
    public ResponseFiles(FileEntity fileEntity) {
        this(
                fileEntity.getFileSeq(),
                fileEntity.getFileName(),
                fileEntity.getFileContent(),
                fileEntity.getFileUrl(),
                fileEntity.getProjectSeq(),
                fileEntity.getCreatedAt());
    }
}
