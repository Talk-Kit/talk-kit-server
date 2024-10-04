package com.canal.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestAddPostedFile {
    @NotNull
    private Long postSeq;

    @NotNull
    private Long fileSeq;

}
