package com.canal.service;

import com.canal.client.UserServiceClient;
import com.canal.security.JwtFilter;
import com.canal.security.JwtUtil;
import com.canal.domain.ProjectEntity;
import com.canal.domain.ProjectRepository;
import com.canal.dto.RequestProjectRecord;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    public ProjectService(ProjectRepository projectRepository, JwtFilter jwtFilter,ModelMapper modelMapper,
                          JwtUtil jwtUtil, UserServiceClient userServiceClient){
        this.projectRepository = projectRepository;
        this.jwtFilter = jwtFilter;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.userServiceClient = userServiceClient;
    }

    public boolean createProject(RequestProjectRecord requestProjectRecord,  HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // entity 저장
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            ProjectEntity projectEntity = modelMapper.map(requestProjectRecord, ProjectEntity.class);
            projectEntity.setUserSeq(userSeq);
            projectRepository.save(projectEntity);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}
