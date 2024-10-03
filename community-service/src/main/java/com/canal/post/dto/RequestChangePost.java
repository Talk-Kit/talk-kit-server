package com.canal.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class RequestChangePost {
    @NotNull
    private String postTitle;

    @NotNull
    private int postType;

    @NotNull
    private String postContent;

    @NotNull
    private String postScope;

    private List<Long> files;

}
