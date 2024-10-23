package com.canal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로젝트리스트를 반환하는 dto")
public record ResponseProjects (Long projectSeq, String projectName){
}
