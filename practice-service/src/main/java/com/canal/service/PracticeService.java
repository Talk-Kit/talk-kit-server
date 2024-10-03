package com.canal.service;

import com.canal.client.ProjectServiceClient;
import com.canal.dto.ResponseFiles;
import com.canal.dto.ResponsePresentationFiles;
import com.canal.dto.ResponseScripts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PracticeService {

    private final ProjectServiceClient projectServiceClient;

    public PracticeService(ProjectServiceClient projectServiceClient) {
        this.projectServiceClient = projectServiceClient;
    }

    public ResponseEntity<?> getScripts(Long projectSeq){
        try{
            List<ResponseScripts> resultList = new ArrayList<>();
            Iterable<ResponseFiles> responseFiles = projectServiceClient.getAllFilesByProject(projectSeq);
            responseFiles.forEach(file -> {
                if (file.fileContent() != null){
                    resultList.add(new ResponseScripts(file.fileSeq(),file.fileName(),file.fileContent(),file.projectSeq()));
                }
            });
            if (resultList.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(resultList);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> getFiles(Long projectSeq){
        try{
            List<ResponsePresentationFiles> resultList = new ArrayList<>();
            Iterable<ResponseFiles> responseFiles = projectServiceClient.getAllFilesByProject(projectSeq);
            responseFiles.forEach(file -> {
                if (file.fileContent() == null){
                    resultList.add(new ResponsePresentationFiles(file.fileSeq(),file.fileName(),file.fileUrl(),file.projectSeq()));
                }
            });
            if (resultList.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(resultList);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    public ResponseEntity<?> saveVoice(MultipartFile file, Long projectSeq, String token){
        return projectServiceClient.saveVoice(file,projectSeq,token);
    }




}
