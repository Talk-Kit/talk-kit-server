package com.canal.client;

import com.canal.dto.RequestNewProject;
import com.canal.dto.RequestScript;
import com.canal.dto.ResponseProjects;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="project-service", configuration = HeaderConfig.class)
public interface ProjectServiceClient {

    @PostMapping("/api/project-service/client/file/{projectSeq}")
    boolean saveScript(@PathVariable("projectSeq") Long projectSeq,
                      @RequestBody RequestScript requestScript);

    @GetMapping("/api/project-service/projects")
    Iterable<ResponseProjects> getAllProjectsByClient(@RequestHeader("Authorization")String auth);

    @PostMapping("/api/project-service/project")
    ResponseEntity<String> createProject(@RequestBody RequestNewProject requestNewProject, @RequestHeader("Authorization")String auth);

}
