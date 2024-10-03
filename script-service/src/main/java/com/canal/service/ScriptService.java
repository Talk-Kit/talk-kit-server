package com.canal.service;

import com.canal.client.OpenAIApiClient;
import com.canal.client.ProjectServiceClient;
import com.canal.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ScriptService {

    private final ProjectServiceClient projectServiceClient;
    private final OpenAIApiClient openAIApiClient;

    @Value("${open-ai.api.model}")
    private String model;
    @Value("${open-ai.api.user}")
    private String user;

    public ScriptService(ProjectServiceClient projectServiceClient,OpenAIApiClient openAIApiClient) {
        this.projectServiceClient = projectServiceClient;
        this.openAIApiClient = openAIApiClient;
    }

    public boolean requestSaveScript(Long projectSeq, RequestScript requestScript){
        boolean success = projectServiceClient.saveScript(projectSeq,requestScript);
        if (!success){ return false;}
        return true;
    }
    public String requestGpt(RequestContent requestContent) {
        try{
            RequestOpenAiApi.Messages messages = new RequestOpenAiApi.Messages(user,requestContent.content());
            RequestOpenAiApi requestOpenAiApi = new RequestOpenAiApi(model, List.of(messages));
            ResponseOpenAiApi response = openAIApiClient.requestGpt(requestOpenAiApi);

            return response.choices().get(0).message().content();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
    public ResponseEntity<?> createProject(RequestNewProject requestNewProject,String auth){
        return projectServiceClient.createProject(requestNewProject,auth);
    }

}
