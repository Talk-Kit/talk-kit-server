package com.canal.dto;


import java.time.LocalDateTime;

public record ResponseFiles(
        Long fileSeq,
        String fileName,
        String fileContent,
        String fileUrl,
        Long projectSeq,
        LocalDateTime createdAt
) {
}
