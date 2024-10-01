package com.canal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="NhnStorageClient",url = "${nhn.storage.url}")
public interface NHNStorageClient {

    @PutMapping(value = "/{Folder}/{Object}", consumes = "application/json", produces = "application/json")
    ResponseEntity<Void> uploadfile(
            @PathVariable("Folder") String folder,
            @PathVariable("Object") String objectName,
            @RequestHeader("X-Auth-Token") String nhnToken,
            @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
            @RequestBody byte[] file);

    @DeleteMapping("/{Folder}/{Object}")
    ResponseEntity<Void> deletefile(
            @PathVariable("Folder")String folder,
            @PathVariable("Object")String objectName,
            @RequestHeader("X-Auth-Token") String nhnToken
    );

}

