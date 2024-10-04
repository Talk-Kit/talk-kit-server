package com.canal.post.controller;

import com.canal.client.ProjectServiceClient;
import com.canal.post.dto.*;
import com.canal.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community-service/post")
@RequiredArgsConstructor
@Tag(name = "Post Controller",description = "게시글 생성과 작성을 위한 컨트롤러입니다")
public class PostController {

    private final PostService postService;
    private final ProjectServiceClient projectServiceClient;

    @Operation(summary = "새 게시글 작성 API", description = "사용자가 작성한 게시글을 저장합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 저장 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 저장 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/new")
    public ResponseEntity<?> addPost(@RequestBody RequestAddPost requestAddPost,
                                     @RequestHeader("Authorization")String auth) {
        return postService.createPost(requestAddPost,auth);
    }

    @Operation(summary = "게시글 수정 API", description = "사용자가 작성한 게시글을 수정합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 수정 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PutMapping("/update/{postSeq}")
    public ResponseEntity<?> updatePost(@RequestBody RequestChangePost requestChangePost,
                                        @PathVariable Long postSeq,
                                        @RequestHeader("Authorization")String auth) {
        return postService.updatePost(requestChangePost, postSeq, auth);
    }

    @Operation(summary = "게시글 삭제 API", description = "사용자가 작성한 게시글을 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 삭제 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @DeleteMapping("/delete/{postSeq}")
    public ResponseEntity<?> deletePost(@PathVariable Long postSeq, @RequestHeader("Authorization")String auth) {
        return postService.delete(postSeq, auth);
    }

    @Operation(summary = "게시판 유형 별 게시글 조회 API", description = "게시글 유형 별로 삭제되지 않은 게시글을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 조회 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/list/{postType}")
    public ResponseEntity<List<ResponsePostRecord>> getAllPostByPostType(@PathVariable int postType) {

        List<ResponsePostRecord> resultList = postService.getAllPostByPostType(postType);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    @Operation(summary = "게시판 유형 별 공개 게시글 조회 API", description = "게시글 유형 별로 삭제되지 않은 공개 게시글을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 조회 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/list/public/{postType}")
    public ResponseEntity<List<ResponsePostRecord>> getAllPublicPostByPostType(@PathVariable int postType) {

        List<ResponsePostRecord> resultList = postService.getAllPublicPostByPostType(postType);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    @Operation(summary = "게시글 검색 API", description = "키워드로 게시글을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 걷색 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 검색 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ResponsePostRecord>> getKeywordPost(@PathVariable String keyword) {

        List<ResponsePostRecord> resultList = postService.getKeywordPost(keyword);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    @Operation(summary = "프로젝트 조회 API", description = "파일을 불러올 프로젝트를 불러옵니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 프로젝트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 프로젝트 조회 실패. userId 확인 필요"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/projects")
    public ResponseEntity<?> getAllProjectsByClient(@RequestHeader("Authorization")String auth) {
        Iterable<ResponseProjects> response = projectServiceClient.getAllProjects(auth);
        if (response == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (!response.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body("생성된 프로젝트 없음");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "파일 조회 API", description = "선택한 프로젝트에 있는 파일을 불러옵니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 파일 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 파일 조회 실패. projectSeq 확인 필요"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/files/{projectSeq}")
    public ResponseEntity<?> getAllFilesByProjectClient(@PathVariable("projectSeq") Long projectSeq) {
        Iterable<ResponseFilesByProject> response = projectServiceClient.getAllFilesByProject(projectSeq);
        if (response == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (!response.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body("생성된 파일 없음");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "단일 파일 조회 API", description = "선택된 하나의 파일을 불러옵니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK: 파일 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 파일 조회 실패. fileSeq 확인 필요"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/file/{fileSeq}")
    public ResponseEntity<?> getFileByFileSeq(@PathVariable("fileSeq") Long fileSeq) {
        ResponseFilesByProject response = projectServiceClient.getFileByFileSeq(fileSeq);
        if (response == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("생성된 파일 없음");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
