package com.canal.post.controller;

import com.canal.post.dto.RequestAddPost;
import com.canal.post.dto.RequestChangePost;
import com.canal.post.dto.ResponsePostRecord;
import com.canal.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    @Operation(summary = "새 게시글 작성 API", description = "사용자가 작성한 게시글을 저장합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 저장 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 저장 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/new")
    public ResponseEntity<?> addPost(@RequestBody RequestAddPost requestAddPost, HttpServletRequest httpServletRequest) {
        return postService.createPost(requestAddPost,httpServletRequest);
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
    public ResponseEntity<?> updatePost(@RequestBody RequestChangePost requestChangePost, @PathVariable Long postSeq, HttpServletRequest httpServletRequest) {
        return postService.updatePost(requestChangePost, postSeq, httpServletRequest);
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
    public ResponseEntity<?> deletePost(@PathVariable Long postSeq, HttpServletRequest httpServletRequest) {
        return postService.delete(postSeq, httpServletRequest);
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

}
