package com.canal.service;

import com.canal.client.NHNStorageClient;
import com.canal.client.UserServiceClient;
import com.canal.domain.FileEntity;
import com.canal.domain.FileRepository;
import com.canal.dto.*;
import com.canal.security.JwtFilter;
import com.canal.security.JwtUtil;
import com.canal.domain.ProjectEntity;
import com.canal.domain.ProjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FileRepository fileRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;
    private final NHNStorageClient nhnStorageClient;
    private final NHNAuthService nhnAuthService;
    private static final Map<String,String> contentTypeMap = new HashMap<>();
    @Value("${nhn.storage.url}")
    private String STORAGE_URL;

    static {
        // 이미지
        contentTypeMap.put("image/jpeg", ".jpeg");
        contentTypeMap.put("image/png", ".png");
        // 발표자료
        contentTypeMap.put("application/pdf", ".pdf");
        contentTypeMap.put("application/vnd.ms-powerpoint", ".ppt");
        contentTypeMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");
        contentTypeMap.put("text/html", ".html");
        // 텍스트
        contentTypeMap.put("application/msword",".doc");
        contentTypeMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document",".docx");
        contentTypeMap.put("text/plain", ".txt");
        // 오디오
        contentTypeMap.put("audio/mpeg ", ".mp3");
        contentTypeMap.put("audio/wav ", ".wav");
        contentTypeMap.put("audio/ogg ", ".ogg");
    }

    public ProjectService(ProjectRepository projectRepository, JwtFilter jwtFilter,ModelMapper modelMapper,
                          JwtUtil jwtUtil, UserServiceClient userServiceClient,
                          NHNStorageClient nhnStorageClient,FileRepository fileRepository,NHNAuthService nhnAuthService){
        this.projectRepository = projectRepository;
        this.jwtFilter = jwtFilter;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.userServiceClient = userServiceClient;
        this.nhnStorageClient = nhnStorageClient;
        this.fileRepository = fileRepository;
        this.nhnAuthService = nhnAuthService;
    }

    public boolean checkProjectName(String projectName,HttpServletRequest httpServletRequest){
        Long userSeq = getUserSeq(httpServletRequest);
        ProjectEntity projectEntity = projectRepository.findByProjectNameAndUserSeqAndDeleted(
                projectName,userSeq,false);

        if(projectEntity == null){
            return false;
        }
        return true;
    }

    public boolean createProject(RequestProject requestProject, HttpServletRequest httpServletRequest){
        try{
            // userSeq 추출
            Long userSeq = getUserSeq(httpServletRequest);
            // entity 저장
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            ProjectEntity projectEntity = modelMapper.map(requestProject, ProjectEntity.class);
            projectEntity.setUserSeq(userSeq);
            projectRepository.save(projectEntity);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean updateProject(Long projectSeq,RequestProject requestProject){
        try {
            ProjectEntity projectEntity = projectRepository.findByProjectSeqAndDeleted(projectSeq,false);
            if (projectEntity == null) {
                return false;
            }
            projectEntity.setProjectName(requestProject.getProjectName());
            projectRepository.save(projectEntity);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteProject(Long projectSeq){
        try{
            Iterable<FileEntity> fileEntity = fileRepository.findByProjectSeqAndDeleted(projectSeq,false);
            if (fileEntity.iterator().hasNext()) {
                fileEntity.forEach(value -> value.setDeleted(true));
                fileRepository.saveAll(fileEntity);
            }
            ProjectEntity projectEntity = projectRepository.findById(projectSeq).orElse(null);
            if (projectEntity == null) {
                log.error("projectSeq에 해당하는 프로젝트 없음");
                return false;
            }
            projectEntity.setDeleted(true);
            projectRepository.save(projectEntity);

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public String uploadFile(MultipartFile file,String nhnToken,HttpServletRequest httpServletRequest){
        try{
            // 스토리지 저장시 userSeq별로 폴더를 생성
            Long userSeq = getUserSeq(httpServletRequest);

            String folder = "u"+userSeq;
            String contentType = file.getContentType();
            String objectName = setRandomFileName(contentType);
            byte[] bytes = file.getBytes();
            ResponseEntity<Void> response = nhnStorageClient.uploadfile(
                    folder,
                    objectName,
                    nhnToken,
                    contentType,
                    bytes
            );
            if (response.getStatusCode().value() != 201){
                return null;
            }
            return STORAGE_URL+"/"+folder+"/"+objectName; // 스토리지 저장 성공시 url 반환

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public boolean saveFiles(String storageUrl,Long projectSeq,String fileName){
        try{
            ProjectEntity projectEntity = projectRepository.findByProjectSeqAndDeleted(projectSeq,false);
            if (projectEntity == null) {
                return false;
            }

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setFileUrl(storageUrl);
            fileEntity.setProjectSeq(projectSeq);
            fileRepository.save(fileEntity);
            return true;
        } catch (Exception e) {

            log.error(e.getMessage());
            return false;
        }
    }
    public boolean saveScript(Long projectSeq,RequestScript requestScript){
        try{
            FileEntity fileEntity = new FileEntity();
            fileEntity.setProjectSeq(projectSeq);
            fileEntity.setFileContent(requestScript.fileContent());
            fileEntity.setFileName(requestScript.fileName());
            fileRepository.save(fileEntity);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
    public ResponseFiles getFile(Long fileSeq){
        try {
            FileEntity fileEntity = fileRepository.findByFileSeqAndDeleted(fileSeq,false);
            if (fileEntity == null) {
                return null;
            }
            return new ResponseFiles(fileEntity);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public Iterable<ResponseProjectsRecord> getAllProjectsByUserId(HttpServletRequest httpServletRequest){
        try {
            List<ResponseProjectsRecord> emptyList = new ArrayList<>();

            Long userSeq = getUserSeq(httpServletRequest);

            Iterable<ProjectEntity> entityList = projectRepository.findByUserSeqAndDeleted(userSeq,false);
            List<ResponseProjectsRecord> projectList = new ArrayList<>();
            entityList.forEach(value -> projectList.add(new ResponseProjectsRecord(value)));
            if (projectList.isEmpty()){
                return emptyList;
            }
            return projectList;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public Iterable<ResponseFiles> getAllFilesByProject(Long projectSeq){
        try{
            List<ResponseFiles> fileList = new ArrayList<>();
            Iterable<FileEntity> entityList = fileRepository.findByProjectSeqAndDeleted(projectSeq,false);
            entityList.forEach(value -> fileList.add(new ResponseFiles(value)));
            if (fileList.isEmpty()){
                return Collections.emptyList();
            }
            return fileList;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public boolean deleteFile(Long fileSeq){
        try{
           FileEntity fileEntity = fileRepository.findByFileSeqAndDeleted(fileSeq,false);
           fileEntity.setDeleted(true);
           fileRepository.save(fileEntity);

           return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에
    public void deleteOldFiles(){
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2); //2주동안 보관
        String nhnToken = nhnAuthService.getNHNToken();

        List<FileEntity> oldFileList = fileRepository.findAllByDeletedAndUpdatedAtBefore(true,twoWeeksAgo);
        oldFileList.forEach(value -> {
            // 디비 삭제
            fileRepository.delete(value);
            // 스토리지 삭제
            String[] variables = getStoragePathVariables(value.getFileUrl()); // 저장된 url에서 폴더,오브젝트명 뽑아냄
            String folder = variables[0];
            String objectName = variables[1];
            nhnStorageClient.deletefile(folder,objectName,nhnToken);
        });
    }
    public Iterable<ResponseProjectsByClient> getAllProjectsByClient(String userId){
        try{
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            Iterable<ProjectEntity> entityList = projectRepository.findByUserSeqAndDeleted(userSeq,false);
            List<ResponseProjectsByClient> projectList = new ArrayList<>();
            entityList.forEach(project-> projectList.add(new ResponseProjectsByClient(project.getProjectSeq(),project.getProjectName())));
            return projectList;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
    @Transactional
    public boolean createProjectByClient(String projectName, String userId){
        try{
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            ProjectEntity projectEntity = projectRepository.findByProjectNameAndUserSeqAndDeleted(projectName,userSeq,false);
            if(projectEntity != null){
                return false;
            }else{
                projectEntity = new ProjectEntity();
                projectEntity.setProjectName(projectName);
                projectEntity.setUserSeq(userSeq);
                projectRepository.save(projectEntity);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private Long getUserSeq(HttpServletRequest httpServletRequest){
        String token = jwtFilter.resolveToken(httpServletRequest);
        String userId = jwtUtil.getUserIdFromJwt(token);
        Long userSeq = userServiceClient.getUserSeqByUserId(userId);
        return userSeq;
    }

    private String setRandomFileName(String contentType){
        String extension = contentTypeMap.get(contentType);
        if (extension == null){
            extension = ".bin";
        }
        return UUID.randomUUID().toString() + extension;
    }

    private String[] getStoragePathVariables(String storageUrl){
        String target = "files/";
        int targetIndex = storageUrl.indexOf(target);

        String filtered = storageUrl.substring(targetIndex+target.length());

        String[] words = filtered.split("/");

        return words;
    }


}
