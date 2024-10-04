package com.canal.reply.controller;

import com.canal.reply.dto.RequestAddReply;
import com.canal.reply.dto.RequestChangeReply;
import com.canal.reply.dto.ResponseReplyRecord;
import com.canal.reply.service.ReplyService;
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
@RequestMapping("/api/community-service/reply")
@RequiredArgsConstructor
@Tag(name = "Reply Controller",description = "댓글, 대댓글 작성을 위한 컨트롤러입니다")
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "새 댓글 작성 API", description = "게시글에 댓글을 작성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글 작성 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/new/{postSeq}")
    public ResponseEntity<?> addReply(@RequestBody RequestAddReply requestAddReply, @PathVariable Long postSeq,
                                      @RequestHeader("Authorization")String auth) {
        boolean success  = replyService.createReply(requestAddReply, postSeq, auth);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글 작성 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글 작성 실패");
        }
    }

    @Operation(summary = "대댓글 작성 API", description = "댓글에 대댓글을 작성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 대댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 대댓글 작성 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/new/{postSeq}/{replySeq}")
    public ResponseEntity<?> addReReply(@RequestBody RequestAddReply requestAddReply,
                                        @PathVariable Long postSeq,
                                        @PathVariable Long replySeq,
                                        @RequestHeader("Authorization")String auth) {
        boolean success = replyService.createReReply(requestAddReply, postSeq, replySeq, auth);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body("대댓글 작성 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("대댓글 작성 실패");
        }
    }

    @Operation(summary = "댓글, 대댓글 수정 API", description = "게시글에 달린 댓글, 대댓글을 수정합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글, 대댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글, 대댓글 수정 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PutMapping("/update/{replySeq}")
    public ResponseEntity<?> updateReply(@RequestBody RequestChangeReply requestChangeReply,
                                         @PathVariable Long replySeq,
                                         @RequestHeader("Authorization")String auth) {
        boolean success  = replyService.updateReply(requestChangeReply, replySeq, auth);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글, 대댓글 수정 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글, 대댓글 수정 실패");
        }
    }

    @Operation(summary = "댓글, 대댓글 삭제 API", description = "게시글의 댓글, 대댓글을 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글, 대댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글, 대댓글 삭제 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @DeleteMapping("/delete/{replySeq}")
    public String deletePost(@PathVariable Long replySeq, @RequestHeader("Authorization")String auth) {
        boolean success = replyService.deleteReply(replySeq, auth);
        if(success) {
            return "댓글, 대댓글 삭제 성공";
        }
        else{
            return "댓글, 대댓글 삭제 실패";
        }
    }

    @Operation(summary = "게시글 별 댓글, 대댓글 조회 API", description = "게시글 별 삭제되지 않은 모든 게시글의 댓글, 대댓글을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글, 대댓글 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글, 대댓글 조회 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/list/{postSeq}")
    public ResponseEntity<List<ResponseReplyRecord>> getAllReplyByPostSeq(@PathVariable Long postSeq) {

        List<ResponseReplyRecord> resultList = replyService.getAllReplyByPostSeq(postSeq);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }
}
