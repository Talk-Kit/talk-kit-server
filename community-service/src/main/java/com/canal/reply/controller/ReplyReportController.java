package com.canal.reply.controller;

import com.canal.reply.domain.ReplyReportEntity;
import com.canal.reply.dto.RequestAddReplyReport;
import com.canal.reply.dto.RequestChangeReplyReport;
import com.canal.reply.dto.ResponseReplyReportRecord;
import com.canal.reply.service.ReplyReportService;
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
@RequestMapping("/api/community-service/reply/report")
@RequiredArgsConstructor
@Tag(name = "ReplyReport Controller",description = "댓글, 대댓글 신고를 위한 컨트롤러입니다")
public class ReplyReportController {

    private final ReplyReportService replyReportService;

    @Operation(summary = "댓글, 대댓글 신고 작성 API", description = "댓글, 대댓글을 신고 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글 신고 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글 신고 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/new/{replySeq}")
    public ResponseEntity<?> addReplyReport(@RequestBody RequestAddReplyReport requestAddReplyReport,
                                            @PathVariable Long replySeq,
                                            @RequestHeader("Authorization")String auth) {
        boolean success = replyReportService.createReplyReport(requestAddReplyReport, replySeq, auth);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글, 대댓글 신고 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글, 대댓글 신고 실패");
        }
    }

    @Operation(summary = "댓글, 대댓글 신고 내용 수정 API", description = "댓글, 대댓글 신고 내용을 수정 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글, 대댓글 신고 내용 수정 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글, 대댓글 신고 내용 수정 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PutMapping("/update/{reportSeq}")
    public ResponseEntity<?> updateReplyReport(@RequestBody RequestChangeReplyReport requestChangeReplyReport,
                                               @PathVariable Long reportSeq,
                                               @RequestHeader("Authorization")String auth) {
        boolean success = replyReportService.updateReplyReport(requestChangeReplyReport, reportSeq, auth);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글, 대댓글 신고 수정 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글, 대댓글 신소 수정 실패");
        }
    }

    @Operation(summary = "댓글, 대댓글 신고 내역 삭제 API", description = "댓글, 대댓글 신고 내역을 삭제 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글, 대댓글 신고 내역 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글, 대댓글 신고 내역 삭제 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @DeleteMapping("/delete/{reportSeq}")
    public ResponseEntity<?> deleteReplyReport(@PathVariable Long reportSeq, @RequestHeader("Authorization")String auth) {
        ReplyReportEntity deletedReplyEntity = replyReportService.deleteReplyReport(reportSeq, auth);
        if(deletedReplyEntity == null) {
            return ResponseEntity.status(HttpStatus.OK).body("댓글, 대댓글 신고 삭제 실패");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글, 대댓글 신고 삭제 성공");
        }
    }

    @Operation(summary = "댓글, 대댓글 신고 내역 조회 API", description = "댓글, 대댓글 신고 내역을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글, 대댓글 신고 내역 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글, 대댓글 신고 내역 조회 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/list")
    public ResponseEntity<List<ResponseReplyReportRecord>> getAllReplyReport() {

        List<ResponseReplyReportRecord> resultList = replyReportService.getAllReplyReport();

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    @Operation(summary = "댓글, 대댓글 신고 내역 조회 API", description = "게시글 별 삭제되지 않은 댓글, 대댓글 신고 내역을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 댓글, 대댓글 신고 내역 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 댓글, 대댓글 신고 내역 삭제 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/list/{replySeq}")
    public ResponseEntity<List<ResponseReplyReportRecord>> getAllReplyReportByReplySeq(@PathVariable Long replySeq) {

        List<ResponseReplyReportRecord> resultList = replyReportService.getAllReplyReportByPostSeq(replySeq);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

}
