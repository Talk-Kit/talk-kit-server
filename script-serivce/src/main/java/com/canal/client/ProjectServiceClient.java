package com.canal.client;

import com.canal.dto.RequestNewProject;
import com.canal.dto.RequestProjects;
import com.canal.dto.RequestScript;
import com.canal.dto.ResponseProjects;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="project-service", configuration = HeaderConfig.class)
public interface ProjectServiceClient {

    @PostMapping("/api/project-service/client/file/{projectSeq}")
    boolean saveScript(@PathVariable("projectSeq") Long projectSeq,
                      @RequestBody RequestScript requestScript);

    @GetMapping("/api/project-service/client/projects/{userId}")
    Iterable<ResponseProjects> getAllProjectsByClient(@PathVariable("userId") String userId);

    @PostMapping("/api/project-service/client/project")
    boolean createProject(@RequestBody RequestNewProject requestNewProject);

}
