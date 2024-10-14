package com.canal.post.controller;

import com.canal.post.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community-service/post/like")
@RequiredArgsConstructor
@Tag(name = "PostLike Controller",description = "게시글 좋아요를 위한 컨트롤러입니다")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 API", description = "게시글에 좋아요, 좋아요 취소를 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 좋아요 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 좋아요 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/{postSeq}")
    public ResponseEntity<Boolean> likePost(@PathVariable Long postSeq, @RequestHeader("Authorization") String auth) {
        // return true: 좋아요 생성, return false: 좋아요 취소
        return postLikeService.likePost(postSeq, auth);
    }

    @Operation(summary = "게시글 별 좋아요 갯수 조회 API", description = "게시글 별 삭제되지 않은 좋아요 갯수를 조회 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 좋아요 갯수 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 좋아요 갯수 조회 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/like-num/{postSeq}")
    public int getAllPostLikeByPostSeq(@PathVariable Long postSeq) {

        return postLikeService.getAllPostLikeByPostSeq(postSeq);
    }

}
