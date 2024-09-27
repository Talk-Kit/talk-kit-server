package com.canal.reply.controller;

import com.canal.reply.domain.ReplyReportEntity;
import com.canal.reply.dto.RequestAddReplyReport;
import com.canal.reply.dto.RequestChangeReplyReport;
import com.canal.reply.dto.ResponseReplyReportRecord;
import com.canal.reply.service.ReplyReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community-service/reply/report")
@RequiredArgsConstructor
public class ReplyReportController {

    private final ReplyReportService replyReportService;

    // 댓글, 대댓글  신고 작성
    @PostMapping("/new/{replySeq}")
    public ResponseEntity<?> addReplyReport(@RequestBody RequestAddReplyReport requestAddReplyReport, @PathVariable Long replySeq, HttpServletRequest httpServletRequest) {
        boolean success = replyReportService.createReplyReport(requestAddReplyReport, replySeq, httpServletRequest);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글, 대댓글 신고 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글, 대댓글 신고 실패");
        }
    }

    // 댓글, 대댓글  신고 수정
    @PutMapping("/update/{reportSeq}")
    public ResponseEntity<?> updateReplyReport(@RequestBody RequestChangeReplyReport requestChangeReplyReport, @PathVariable Long reportSeq, HttpServletRequest httpServletRequest) {
        boolean success = replyReportService.updateReplyReport(requestChangeReplyReport, reportSeq, httpServletRequest);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글, 대댓글 신고 수정 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글, 대댓글 신소 수정 실패");
        }
    }

    // 댓글, 대댓글 신고 삭제
    @DeleteMapping("/delete/{reportSeq}")
    public String deleteReplyReport(@PathVariable Long reportSeq, HttpServletRequest httpServletRequest) {
        ReplyReportEntity deletedReplyEntity = replyReportService.deleteReplyReport(reportSeq, httpServletRequest);
        if(deletedReplyEntity == null) {
            return "댓글, 대댓글 신고 삭제 실패";
        }
        else{
            return "댓글, 대댓글 신고 삭제 성공";
        }
    }

    // 삭제되지 않은 댓글, 대댓글 신고 내역 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<ResponseReplyReportRecord>> getAllReplyReport() {

        List<ResponseReplyReportRecord> resultList = replyReportService.getAllReplyReport();

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    // 댓글 별 삭제되지 않은 댓글, 대댓글 신고 내역 가져오기
    @GetMapping("/list/{replySeq}")
    public ResponseEntity<List<ResponseReplyReportRecord>> getAllReplyReportByReplySeq(@PathVariable Long replySeq) {

        List<ResponseReplyReportRecord> resultList = replyReportService.getAllReplyReportByPostSeq(replySeq);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

}
