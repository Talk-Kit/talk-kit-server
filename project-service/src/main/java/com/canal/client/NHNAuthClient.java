package com.canal.client;

import com.canal.dto.RequestNHNToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name="NhnAuthClient",url = "${nhn.auth.url}")
public interface NHNAuthClient {

    @PostMapping(value = "/tokens",consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getNHNToken(@RequestBody RequestNHNToken requestNHNToken);

}

