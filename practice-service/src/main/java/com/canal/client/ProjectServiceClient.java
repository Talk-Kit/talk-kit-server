package com.canal.client;

import com.canal.dto.ResponseFiles;
import com.canal.dto.ResponseProjects;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name="project-service", configuration = FeignConfig.class)
public interface ProjectServiceClient {

    // 프로젝트 불러올때
    @GetMapping("/api/project-service/projects")
    Iterable<ResponseProjects> getAllProjectsByClient(@RequestHeader("Authorization")String auth);

    // 파일 불러올때
    @GetMapping("/api/project-service/files/{projectSeq}")
    Iterable<ResponseFiles> getAllFilesByProject(@PathVariable("projectSeq")Long projectSeq);

    //음성녹음 저장
    @PostMapping(value = "/api/project-service/file/{projectSeq}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> saveVoice(@RequestPart("file") MultipartFile file ,
                                     @PathVariable("projectSeq")Long projectSeq,
                                     @RequestHeader("Authorization")String auth);
}
