package com.canal.dto;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


public record ResponsePresentationFiles(
        Long fileSeq,
        String fileName,
        @Parameter(content = @Content(contentSchema = @Schema(implementation = String.class) ))
        String fileUrl,
        Long projectSeq
) {
}
