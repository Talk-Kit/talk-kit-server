package com.canal.post.controller;

import com.canal.post.domain.PostReportEntity;
import com.canal.post.dto.RequestAddPostReport;
import com.canal.post.dto.RequestChangePostReport;
import com.canal.post.dto.ResponsePostReportRecord;
import com.canal.post.service.PostReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community-service/post/report")
@RequiredArgsConstructor
public class PostReportController {

    private final PostReportService postReportService;

    // 게시글 신고 작성
    @PostMapping("/new/{postSeq}")
    public ResponseEntity<?> addPostReport(@RequestBody RequestAddPostReport requestAddPostReport, @PathVariable Long postSeq, HttpServletRequest httpServletRequest) {
        return postReportService.createPostReport(requestAddPostReport, postSeq, httpServletRequest);
    }

    // 게시글 신고 수정
    @PutMapping("/update/{reportSeq}")
    public ResponseEntity<?> updatePostReport(@RequestBody RequestChangePostReport requestChangePostReport, @PathVariable Long reportSeq, HttpServletRequest httpServletRequest) {
        return postReportService.updatePostReport(requestChangePostReport, reportSeq, httpServletRequest);
    }

    // 게시글 신고 삭제
    @DeleteMapping("/delete/{reportSeq}")
    public ResponseEntity<?> deletePostReport(@PathVariable Long reportSeq, HttpServletRequest httpServletRequest) {
        return postReportService.deletePostReport(reportSeq, httpServletRequest);
    }

    // 삭제되지 않은 게시글 신고 내역 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<ResponsePostReportRecord>> getAllPostReport() {

        List<ResponsePostReportRecord> resultList = postReportService.getAllPostReport();

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    // 게시글 별 삭제되지 않은 게시글 신고 내역 가져오기
    @GetMapping("/list/{postSeq}")
    public ResponseEntity<List<ResponsePostReportRecord>> getAllPostReportByPostSeq(@PathVariable Long postSeq) {

        List<ResponsePostReportRecord> resultList = postReportService.getAllPostReportByPostSeq(postSeq);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

}
