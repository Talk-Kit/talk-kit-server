package com.canal.controller;

import com.canal.dto.*;
import com.canal.service.NHNAuthService;
import com.canal.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/project-service")
@Slf4j
@Tag(name = "Project Controller",description = "프로젝트와 파일을 저장하고 관리하기 위한 컨트롤러입니다. 공통되는 에러코드는 아래와 같습니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
        @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
})
public class ProjectController {

    private final ProjectService projectService;
    private final NHNAuthService nhnAuthService;

    public ProjectController(ProjectService projectService,NHNAuthService nhnAuthService) {
        this.projectService = projectService;
        this.nhnAuthService = nhnAuthService;
    }

    @Operation(summary = "프로젝트 생성 API", description = "프로젝트를 생성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "CREATED: 생성 성공"),
            @ApiResponse(responseCode = "409", description = "CONFLICT: 중복된 프로젝트명"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 프로젝트 생성 실패"),
    })
    @PostMapping("/project")
    public ResponseEntity<String> createProject(
            @RequestBody RequestProject requestProject,
            @RequestHeader("Authorization")String token) {

        // 중복된 프로젝트명인지 확인
        boolean duplicatedName = projectService.checkProjectName(requestProject.getProjectName(),token);
        if (duplicatedName) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 프로젝트명");
        }
        // 프로젝트 생성
        boolean success  = projectService.createProject(requestProject,token);
        if (!success){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("프로젝트 생성 실패");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 생성 성공");
    }

    @Operation(summary = "프로젝트 수정 API", description = "프로젝트명을 수정합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 수정 성공"),
            @ApiResponse(responseCode = "409", description = "CONFLICT: 변경하려는 프로젝트명이 이미 존재합니다."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: projectSeq에 해당하는 프로젝트가 없습니다."),
    })
    @PutMapping("/project/update/{projectSeq}")
    public ResponseEntity<String> updateProject(@PathVariable("projectSeq")Long projectSeq,
                                                @RequestBody RequestProject requestProject,
                                                @RequestHeader("Authorization")String token) {
        // 변경하려는 프로젝트명이 존재하는지 확인
        boolean duplicatedName = projectService.checkProjectName(requestProject.getProjectName(),token);
        if (duplicatedName) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("변경하려는 프로젝트명이 이미 존재합니다.");
        }
        // 프로젝트명 업데이트
        boolean success = projectService.updateProject(projectSeq,requestProject);
        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("projectSeq에 해당하는 프로젝트가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("업데이트 성공");
    }

    @Operation(summary = "프로젝트 삭제 API", description = "프로젝트를 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: projectSeq에 해당하는 프로젝트가 없습니다."),
    })
    @DeleteMapping("/project/delete/{projectSeq}")
    public ResponseEntity<String> deleteProject(@PathVariable("projectSeq")Long projectSeq){
        boolean success = projectService.deleteProject(projectSeq);
        if (!success){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("projectSeq에 해당하는 프로젝트가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("프로젝트 삭제 성공");
    }

    @Operation(summary = "프로젝트 조회 API",
            description = "사용자별 프로젝트 목록을 조회합니다. Authorization 헤더의 jwt 토큰으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 조회 성공"),
            @ApiResponse(responseCode = "202 ", description = "ACCEPTED: 조회 성공이지만 생성된 프로젝트 없음"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 조회실패"),
    })
    @GetMapping("/projects")
    public ResponseEntity<?> getProjectsByUserId(@RequestHeader("Authorization")String token){
        Iterable<ResponseProjectsRecord> responseProjectsRecord = projectService.getAllProjectsByUserId(token);
        if (responseProjectsRecord == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("프로젝트 조회 실패");
        }
        if (!responseProjectsRecord.iterator().hasNext()){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("생성된 프로젝트 없음");
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseProjectsRecord);
    }

    @Operation(summary = "파일 업로드 API", description = "프로젝트명에 파일을 업로드합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 파일 저장 성공"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED: NHN 스토리지 토큰 발급 실패"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 스토리지 및 디비 저장 실패. 요청값을 확인해주세요"),

    })
    @PostMapping(value = "/file/{projectSeq}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart("file") MultipartFile file ,
            @PathVariable("projectSeq")Long projectSeq,
            @RequestHeader("Authorization")String authorization
    ){

        // nhn 토큰 발급
        String nhnToken = nhnAuthService.getNHNToken();
        if (nhnToken == null){ // 발급 실패시
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NHN 토큰 발급 실패");
        }
        // 스토리지 업로드
        String storageUrl = projectService.uploadFile(file,nhnToken,authorization);
        if (storageUrl == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("스토리지 업로드 실패: 요청값을 확인해주세요");
        }
        // 디비 저장
        boolean success = projectService.saveFiles(storageUrl,projectSeq,file.getOriginalFilename());
        if (!success){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("디비 저장 실패: 요청값을 확인해주세요");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("파일 저장 성공");
    }


    @Operation(summary = "파일 목록 조회 API", description = "프로젝트별 파일 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 파일 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: projectSeq에 해당하는 프로젝트가 없습니다"),
    })
    @GetMapping("/files/{projectSeq}")
    public ResponseEntity<?> getAllFilesByProject(@PathVariable("projectSeq")Long projectSeq){
        Iterable<ResponseFiles> responseFiles = projectService.getAllFilesByProject(projectSeq);
        if (responseFiles == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("projectSeq에 해당하는 프로젝트가 없습니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseFiles);
    }

    @Operation(summary = "파일 조회 API", description = "파일의 상세 정보를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 파일 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: fileSeq에 해당하는 파일이 없습니다"),
    })
    @GetMapping("/file/{fileSeq}")
    public ResponseEntity<?> getFile(@PathVariable("fileSeq")Long fileSeq){
        ResponseFiles responseFiles = projectService.getFile(fileSeq);
        if (responseFiles == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fileSeq에 해당하는 파일이 없습니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseFiles);
    }

    @Operation(summary = "파일 삭제 API", description = "파일을 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 삭제완료"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: fileSeq에 해당하는 파일이 없습니다."),
    })
    @DeleteMapping("/file/delete/{fileSeq}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileSeq") Long fileSeq){
        boolean isDeleted = projectService.deleteFile(fileSeq);
        if (!isDeleted){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fileSeq에 해당하는 파일이 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("삭제완료");
    }
    /*for feign client*/
    @Operation(hidden = true)
    @PostMapping("/client/file/{projectSeq}")
    public boolean saveScript(@PathVariable("projectSeq")Long projectSeq,
                                        @RequestBody RequestScript requestScript){
        return projectService.saveScript(projectSeq,requestScript);
    }


}
