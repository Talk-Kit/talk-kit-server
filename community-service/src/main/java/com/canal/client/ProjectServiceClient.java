package com.canal.client;

import com.canal.post.dto.ResponseFilesByProject;
import com.canal.post.dto.ResponseProjects;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="project-service", configuration = FeignConfig.class)
public interface ProjectServiceClient {

    @GetMapping("/api/project-service/projects")
    Iterable<ResponseProjects> getAllProjects(@RequestHeader("Authorization")String auth);

    @GetMapping("/api/project-service/files/{projectSeq}")
    Iterable<ResponseFilesByProject> getAllFilesByProject(@PathVariable("projectSeq") Long projectSeq);

    @GetMapping("/api/project-service/file/{fileSeq}")
    ResponseFilesByProject getFileByFileSeq(@PathVariable("fileSeq") Long fileSeq);
}
