package com.canal.controller;

import com.canal.client.ProjectServiceClient;
import com.canal.dto.*;
import com.canal.service.ScriptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/script-service")
public class ScriptController {

    private final ScriptService scriptService;
    private final ProjectServiceClient projectServiceClient;

    public ScriptController(ScriptService scriptService,ProjectServiceClient projectServiceClient) {
        this.scriptService = scriptService;
        this.projectServiceClient = projectServiceClient;
    }

    @PostMapping("/script/{projectSeq}")
    public ResponseEntity<String> createScript(@PathVariable("projectSeq") Long projectSeq,
                                               @RequestBody RequestScript requestScript) {
        boolean success = scriptService.requestSaveScript(projectSeq,requestScript);
        if(!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("대본 저장 실패");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("대본 저장 성공");
    }

    @PostMapping("/script/ai")
    public ResponseEntity<String> requestGpt(@RequestBody RequestContent requestContent) {
        String response = scriptService.requestGpt(requestContent);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("대본 초안 생성 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/project")
    public ResponseEntity<String> createProject(@RequestBody RequestNewProject requestNewProject) {
        boolean success = projectServiceClient.createProject(requestNewProject);
        if(!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 프로젝트명 또는 없는 사용자");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 생성 성공");
    }

    @GetMapping("/projects/{userId}")
    public ResponseEntity<?> getAllProjects(@PathVariable("userId") String userId) {
        Iterable<ResponseProjects> response = projectServiceClient.getAllProjectsByClient(userId);
        if (response == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (!response.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body("생성된 프로젝트 없음");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
