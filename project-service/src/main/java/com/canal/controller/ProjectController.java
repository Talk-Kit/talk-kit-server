package com.canal.controller;

import com.canal.dto.*;
import com.canal.service.NHNAuthService;
import com.canal.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/project-service")
@Slf4j
public class ProjectController {

    private final ProjectService projectService;
    private final NHNAuthService nhnAuthService;

    public ProjectController(ProjectService projectService,NHNAuthService nhnAuthService) {
        this.projectService = projectService;
        this.nhnAuthService = nhnAuthService;
    }

    @PostMapping("/project")
    public ResponseEntity<String> createProject(
            @RequestBody RequestProject requestProject,
            HttpServletRequest httpServletRequest) {

        // 중복된 프로젝트명인지 확인
        boolean duplicatedName = projectService.checkProjectName(requestProject.getProjectName(),httpServletRequest);
        if (duplicatedName) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 프로젝트명");
        }
        // 프로젝트 생성
        boolean success  = projectService.createProject(requestProject,httpServletRequest);
        if (!success){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("프로젝트 생성 실패");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 생성 성공");
    }


    @PutMapping("/update/project/{projectSeq}")
    public ResponseEntity<String> updateProject(@PathVariable("projectSeq")Long projectSeq,
                                                @RequestBody RequestProject requestProject,
                                                HttpServletRequest httpServletRequest) {
        // 변경하려는 프로젝트명이 존재하는지 확인
        boolean duplicatedName = projectService.checkProjectName(requestProject.getProjectName(),httpServletRequest);
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

    @DeleteMapping("/delete/project/{projectSeq}")
    public ResponseEntity<String> deleteProject(@PathVariable("projectSeq")Long projectSeq){
        boolean success = projectService.deleteProject(projectSeq);
        if (!success){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("projectSeq에 해당하는 프로젝트가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("프로젝트 삭제 성공");
    }

    @GetMapping("/projects")
    public ResponseEntity<?> getProjectsByUserId(HttpServletRequest httpServletRequest){
        Iterable<ResponseProjectsRecord> responseProjectsRecord = projectService.getAllProjectsByUserId(httpServletRequest);
        if (responseProjectsRecord == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("프로젝트 조회 실패");
        }
        if (!responseProjectsRecord.iterator().hasNext()){
            return ResponseEntity.status(HttpStatus.OK).body("생성된 프로젝트 없음");
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseProjectsRecord);
    }

    @PostMapping(value = "/file/{projectSeq}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
                                             @PathVariable("projectSeq")Long projectSeq,
                                             HttpServletRequest httpServletRequest){
        // nhn 토큰 발급
        String nhnToken = nhnAuthService.getNHNToken();
        if (nhnToken == null){ // 발급 실패시
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NHN 토큰 발급 실패");
        }
        // 스토리지 업로드
        String storageUrl = projectService.uploadFile(file,nhnToken,httpServletRequest);
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

    @GetMapping("/files/{projectSeq}")
    public ResponseEntity<?> getAllFilesByProject(@PathVariable("projectSeq")Long projectSeq){
        Iterable<ResponseFiles> responseFiles = projectService.getAllFilesByProject(projectSeq);
        if (responseFiles == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("projectSeq에 해당하는 프로젝트가 없습니다");
        }
        if (!responseFiles.iterator().hasNext()){
            return ResponseEntity.status(HttpStatus.OK).body("생성된 파일 없음");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseFiles);
    }

    @GetMapping("/file/{fileSeq}")
    public ResponseEntity<?> getFile(@PathVariable("fileSeq")Long fileSeq){
        ResponseFiles responseFiles = projectService.getFile(fileSeq);
        if (responseFiles == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fileSeq에 해당하는 파일이 없습니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseFiles);
    }


    @DeleteMapping("/delete/file/{fileSeq}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileSeq") Long fileSeq){
        boolean isDeleted = projectService.deleteFile(fileSeq);
        if (!isDeleted){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fileSeq에 해당하는 파일이 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("삭제완료");
    }


}
