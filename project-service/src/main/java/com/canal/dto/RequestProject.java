package com.canal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "프로젝트 생성 및 수정에 사용되는 DTO입니다.")
public class RequestProject {
        @Schema(description = "프로젝트명")
        String projectName;
}
