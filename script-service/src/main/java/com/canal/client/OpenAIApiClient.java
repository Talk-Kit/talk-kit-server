package com.canal.client;

import com.canal.dto.RequestOpenAiApi;
import com.canal.dto.ResponseOpenAiApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="openAIApiClient",url = "${open-ai.api.url}",configuration = HeaderConfig.class)
public interface OpenAIApiClient {

    @PostMapping(value = "/completions", consumes = "application/json", produces = "application/json")
    ResponseOpenAiApi requestGpt(
            @RequestBody RequestOpenAiApi requestOpenAiApi);

}

