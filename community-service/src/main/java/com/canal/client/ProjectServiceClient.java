package com.canal.client;

import com.canal.post.dto.ResponseFilesByProject;
import com.canal.post.dto.ResponseProjects;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="project-service", configuration = FeignConfig.class)
public interface ProjectServiceClient {

    @GetMapping("/api/project-service/client/projects/{userId}")
    Iterable<ResponseProjects> getAllProjectsByClient(@PathVariable("userId") String userId);

    @GetMapping("/api/project-service/client/files/{projectSeq}")
    Iterable<ResponseFilesByProject> getAllFilesByProjectClient(@PathVariable("projectSeq") Long projectSeq);

    @GetMapping("/api/project-service/client/file/{fileSeq}")
    ResponseFilesByProject getFileByFileSeq(@PathVariable("fileSeq") Long fileSeq);
}
