package com.canal.post.controller;

import com.canal.post.domain.PostReportEntity;
import com.canal.post.dto.RequestAddPostReport;
import com.canal.post.dto.RequestChangePostReport;
import com.canal.post.dto.ResponsePostReportRecord;
import com.canal.post.service.PostReportService;
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
@RequestMapping("/api/community-service/post/report")
@RequiredArgsConstructor
@Tag(name = "PostReport Controller",description = "게시글 신고를 위한 컨트롤러입니다")
public class PostReportController {

    private final PostReportService postReportService;

    @Operation(summary = "게시글 신고 작성 API", description = "게시글을 신고 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 신고 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 신고 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PostMapping("/new/{postSeq}")
    public ResponseEntity<?> addPostReport(
            @RequestBody RequestAddPostReport requestAddPostReport,
            @PathVariable Long postSeq,
            @RequestHeader("Authorization")String auth) {
        return postReportService.createPostReport(requestAddPostReport, postSeq, auth);
    }

    @Operation(summary = "게시글 신고 내용 수정 API", description = "게시글 신고 내용을 수정 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 신고 내용 수정 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 신고 내용 수정 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @PutMapping("/update/{reportSeq}")
    public ResponseEntity<?> updatePostReport(
            @RequestBody RequestChangePostReport requestChangePostReport,
            @PathVariable Long reportSeq,
            @RequestHeader("Authorization")String auth) {
        return postReportService.updatePostReport(requestChangePostReport, reportSeq, auth);
    }

    @Operation(summary = "게시글 신고 내역 삭제 API", description = "게시글 신고 내역을 삭제 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 신고 내역 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 신고 내역 삭제 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @DeleteMapping("/delete/{reportSeq}")
    public ResponseEntity<?> deletePostReport(@PathVariable Long reportSeq, @RequestHeader("Authorization")String auth) {
        return postReportService.deletePostReport(reportSeq, auth);
    }

    @Operation(summary = "게시글 신고 내역 조회 API", description = "삭제되지 않은 모든 게시글 신고 내역을 조회 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 신고 내용 수정 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 신고 내용 수정 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/list")
    public ResponseEntity<List<ResponsePostReportRecord>> getAllPostReport() {

        List<ResponsePostReportRecord> resultList = postReportService.getAllPostReport();

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    @Operation(summary = "게시글 별 신고 내역 조회 API", description = "게시글 별 삭제되지 않은 모든 신고 내역을 조회 합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED: 게시글 신고 내용 수정 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: 게시글 신고 내용 수정 실패. 요청값 확인 필요합니다"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
            @ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
    })
    @GetMapping("/list/{postSeq}")
    public ResponseEntity<List<ResponsePostReportRecord>> getAllPostReportByPostSeq(@PathVariable Long postSeq) {

        List<ResponsePostReportRecord> resultList = postReportService.getAllPostReportByPostSeq(postSeq);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

}
