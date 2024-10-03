package com.canal.controller;

import com.canal.client.ProjectServiceClient;
import com.canal.dto.ResponseProjects;
import com.canal.service.PracticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/practice-service")
@Slf4j
public class PracticeController {

    private final PracticeService practiceService;
    private final ProjectServiceClient projectServiceClient;


    public PracticeController(PracticeService practiceService, ProjectServiceClient projectServiceClient) {
        this.projectServiceClient = projectServiceClient;
        this.practiceService = practiceService;
    }

    @Operation(summary = "프로젝트목록 조회 API", description = "사용자별 프로젝트 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 대본 저장 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/projects")
    public ResponseEntity<?> getAllProjects(@RequestHeader("Authorization")String authorization) {
        Iterable<ResponseProjects> response = projectServiceClient.getAllProjectsByClient(authorization);
        if (response == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("프로젝트 조회 실패");
        } else if (!response.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body("생성된 프로젝트 없음");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "대본목록 조회 API", description = "프로젝트별 대본을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 조회 성공"),
            @ApiResponse(responseCode = "204", description = "NO CONTENT: 조회는 성공이나 저장된 대본 없음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 대본 저장 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/scripts/{projectSeq}")
    public ResponseEntity<?> getAllScripts(@PathVariable("projectSeq") Long projectSeq) {
        return practiceService.getScripts(projectSeq);
    }

    @Operation(summary = "파일목록 조회 API", description = "프로젝트별 파일(발표자료)를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 조회 성공"),
            @ApiResponse(responseCode = "204", description = "NO CONTENT: 조회는 성공이나 저장된 대본 없음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 대본 저장 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/files/{projectSeq}")
    public ResponseEntity<?> getAllFiles(@PathVariable("projectSeq") Long projectSeq) {
        return practiceService.getFiles(projectSeq);
    }

    @Operation(summary = "음성녹음 저장 API", description = "프로젝트에 음성녹음을 저장합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 저장 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 대본 저장 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping(value = "/voice/{projectSeq}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveVoice(
            @RequestPart("file") MultipartFile file,
            @PathVariable("projectSeq") Long projectSeq,
            @RequestHeader("Authorization")String authorization
    ) {
        return practiceService.saveVoice(file,projectSeq,authorization);
    }
}
