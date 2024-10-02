package com.canal.controller;

import com.canal.client.ProjectServiceClient;
import com.canal.dto.*;
import com.canal.service.ScriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/script-service")
@Tag(name = "Script Controller",description = "대본 초안 생성과 대본 작성을 위한 컨트롤러입니다")
public class ScriptController {

    private final ScriptService scriptService;
    private final ProjectServiceClient projectServiceClient;

    public ScriptController(ScriptService scriptService,ProjectServiceClient projectServiceClient) {
        this.scriptService = scriptService;
        this.projectServiceClient = projectServiceClient;
    }

    @Operation(summary = "대본 저장 API", description = "사용자가 작성한 대본을 저장합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 저장 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 대본 저장 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/script/{projectSeq}")
    public ResponseEntity<String> createScript(@PathVariable("projectSeq") Long projectSeq,
                                               @RequestBody RequestScript requestScript) {
        boolean success = scriptService.requestSaveScript(projectSeq,requestScript);
        if(!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("대본 저장 실패");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("대본 저장 성공");
    }

    @Operation(summary = "대본 초안 생성 API", description = "chat-gpt로 초안을 생성합니다.(로딩 평균 10초)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 저장 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: aip 요청 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/script/ai")
    public ResponseEntity<String> requestGpt(@RequestBody RequestContent requestContent) {
        String response = scriptService.requestGpt(requestContent);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("대본 초안 생성 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "새 프로젝트 생성 API", description = "대본 저장시 새프로젝트를 생성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 프로젝트 생성 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 프로젝트 저장 실패. 중복된 프로젝트명 또는 없는 사용자"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/project")
    public ResponseEntity<String> createProject(@RequestBody RequestNewProject requestNewProject) {
        boolean success = projectServiceClient.createProject(requestNewProject);
        if(!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 프로젝트명 또는 없는 사용자");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 생성 성공");
    }

    @Operation(summary = "프로젝트 조회 API", description = "대본을 저장할 프로젝트 목록을 불러옵니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 프로젝트 조회 실패. userId 확인 필요"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
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
