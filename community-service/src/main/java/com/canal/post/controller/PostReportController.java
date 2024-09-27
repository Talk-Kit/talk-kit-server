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
        boolean success = postReportService.createPostReport(requestAddPostReport, postSeq, httpServletRequest);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("게시물 신고 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 신고 실패");
        }
    }

    // 게시글 신고 수정
    @PutMapping("/update/{reportSeq}")
    public ResponseEntity<?> updatePostReport(@RequestBody RequestChangePostReport requestChangePostReport, @PathVariable Long reportSeq, HttpServletRequest httpServletRequest) {
        boolean success = postReportService.updatePostReport(requestChangePostReport, reportSeq, httpServletRequest);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("게시물 신고 수정 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 신소 수정 실패");
        }
    }

    // 게시글 신고 삭제
    @DeleteMapping("/delete/{reportSeq}")
    public String deletePostReport(@PathVariable Long reportSeq, HttpServletRequest httpServletRequest) {
        PostReportEntity deletedPostEntity = postReportService.deletePostReport(reportSeq, httpServletRequest);
        if(deletedPostEntity == null) {
            return "게시물 신고 삭제 실패";
        }
        else{
            return "게시물 신고 삭제 성공";
        }
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
