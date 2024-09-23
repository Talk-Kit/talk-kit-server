package com.canal.controller;

import com.canal.dto.RequestProjectRecord;
import com.canal.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project-service")
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {this.projectService = projectService;}

    @PostMapping("/project")
    public ResponseEntity<?> createProject(
            @RequestBody RequestProjectRecord requestProjectRecord,
            HttpServletRequest httpServletRequest) {
        boolean success  = projectService.createProject(requestProjectRecord,httpServletRequest);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("프로젝트 생성 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("프로젝트 생성 실패 test");
        }
    }
}
