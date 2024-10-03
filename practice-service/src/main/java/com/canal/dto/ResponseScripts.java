package com.canal.dto;


public record ResponseScripts(
        Long fileSeq,
        String fileName,
        String fileContent,
        Long projectSeq
) {
}
