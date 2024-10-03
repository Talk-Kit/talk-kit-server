package com.canal.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class RequestAddPostedFile {
    @NotNull
    private Long postSeq;

    @NotNull
    private Long fileSeq;

}
