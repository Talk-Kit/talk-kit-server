package com.canal.reply.controller;

import com.canal.reply.domain.ReplyLikeEntity;
import com.canal.reply.service.ReplyLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community-service/reply/like")
@RequiredArgsConstructor
@Tag(name = "ReplyLike Controller",description = "댓글 좋아요를 위한 컨트롤러입니다")
public class ReplyLikeController {

    private final ReplyLikeService replyLikeService;

    @Operation(summary = "댓글 좋아요 API", description = "댓글에 좋아요를 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글 좋아요 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글 좋아요 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/new/{replySeq}")
    public ResponseEntity<?> addReplyLike(@PathVariable Long replySeq, @RequestHeader("Authorizaiton")String auth) {
        boolean success = replyLikeService.createReplyLike(replySeq, auth);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글 좋아요 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글 좋아요 실패");
        }
    }

    @Operation(summary = "댓글 좋아요 취소 API", description = "댓글에 좋아요를 취소 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글 좋아요 취소 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @DeleteMapping("/delete/{likeSeq}")
    public ResponseEntity<?> deleteReplyLike(@PathVariable Long likeSeq,@RequestHeader("Authorizaiton")String auth) {
        ReplyLikeEntity deletedPostLikeEntity = replyLikeService.deleteReplyLike(likeSeq, auth);
        if(deletedPostLikeEntity == null) {
            return ResponseEntity.status(HttpStatus.OK).body("댓글 좋아요 취소 실패");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글 좋아요 취소 성공");
        }
    }

    @Operation(summary = "댓글 별 좋아요 조회 API", description = "댓글 별 삭제되지 않은 모든 좋아요를 조회 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글 좋아요 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글 좋아요 조회 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/like-num/{replySeq}")
    public int getAllReplyLikeByPostSeq(@PathVariable Long replySeq) {

        return replyLikeService.getAllReplyLikeByPostSeq(replySeq);
    }

}
