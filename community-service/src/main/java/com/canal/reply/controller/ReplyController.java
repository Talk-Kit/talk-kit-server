package com.canal.reply.controller;

import com.canal.reply.dto.RequestAddReply;
import com.canal.reply.dto.RequestChangeReply;
import com.canal.reply.dto.ResponseReplyRecord;
import com.canal.reply.service.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community-service/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    // 댓글 작성
    @PostMapping("/new/{postSeq}")
    public ResponseEntity<?> addReply(@RequestBody RequestAddReply requestAddReply, @PathVariable Long postSeq, HttpServletRequest httpServletRequest) {
        boolean success  = replyService.createReply(requestAddReply, postSeq, httpServletRequest);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글 작성 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글 작성 실패");
        }
    }

    // 대댓글 작성
    @PostMapping("/new/{postSeq}/{replySeq}")
    public ResponseEntity<?> addReReply(@RequestBody RequestAddReply requestAddReply, @PathVariable Long postSeq, @PathVariable Long replySeq, HttpServletRequest httpServletRequest) {
        boolean success = replyService.createReReply(requestAddReply, postSeq, replySeq, httpServletRequest);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body("대댓글 작성 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("대댓글 작성 실패");
        }
    }

    // 댓글, 대댓글 수정
    @PutMapping("/update/{replySeq}")
    public ResponseEntity<?> updateReply(@RequestBody RequestChangeReply requestChangeReply, @PathVariable Long replySeq, HttpServletRequest httpServletRequest) {
        boolean success  = replyService.updateReply(requestChangeReply, replySeq, httpServletRequest);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글, 대댓글 수정 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글, 대댓글 수정 실패");
        }
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{replySeq}")
    public String deletePost(@PathVariable Long replySeq, HttpServletRequest httpServletRequest) {
        boolean success = replyService.deleteReply(replySeq, httpServletRequest);
        if(success) {
            return "댓글, 대댓글 삭제 성공";
        }
        else{
            return "댓글, 대댓글 삭제 실패";
        }
    }

    // 삭제되지 않은 모든 댓글, 대댓글 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<ResponseReplyRecord>> getAllPost() {

        List<ResponseReplyRecord> resultList = replyService.getAllReply();

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    // 게시글 별 삭제되지 않은 모든 댓글, 대댓글 가져오기
    @GetMapping("/list/{postSeq}")
    public ResponseEntity<List<ResponseReplyRecord>> getAllReplyByPostSeq(@PathVariable Long postSeq) {

        List<ResponseReplyRecord> resultList = replyService.getAllReplyByPostSeq(postSeq);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }
}
