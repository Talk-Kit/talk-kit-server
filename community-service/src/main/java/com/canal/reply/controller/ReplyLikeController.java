package com.canal.reply.controller;

import com.canal.reply.domain.ReplyLikeEntity;
import com.canal.reply.dto.RequestAddReplyLike;
import com.canal.reply.dto.ResponseReplyLikeRecord;
import com.canal.reply.service.ReplyLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community-service/reply/like")
@RequiredArgsConstructor
public class ReplyLikeController {

    private final ReplyLikeService replyLikeService;

    // 댓글 좋아요
    @PostMapping("/new/{replySeq}")
    public ResponseEntity<?> addReplyLike(@RequestBody RequestAddReplyLike requestAddReplyLike, @PathVariable Long replySeq, HttpServletRequest httpServletRequest) {
        boolean success = replyLikeService.createReplyLike(requestAddReplyLike, replySeq, httpServletRequest);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("댓글 좋아요 성공");
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("댓글 좋아요 실패");
        }
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/delete/{likeSeq}")
    public String deleteReplyLike(@PathVariable Long likeSeq, HttpServletRequest httpServletRequest) {
        ReplyLikeEntity deletedPostLikeEntity = replyLikeService.deleteReplyLike(likeSeq, httpServletRequest);
        if(deletedPostLikeEntity == null) {
            return "댓글 좋아요 취소 실패";
        }
        else{
            return "댓글 좋아요 취소 성공";
        }
    }

    // 모든 댓글 삭제되지 않은 좋아요 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<ResponseReplyLikeRecord>> getAllReplyLike() {

        List<ResponseReplyLikeRecord> resultList = replyLikeService.getAllReplyLike();

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    // 댓글 별 삭제되지 않은 좋아요 가져오기
    @GetMapping("/list/{replySeq}")
    public ResponseEntity<List<ResponseReplyLikeRecord>> getAllReplyLikeByPostSeq(@PathVariable Long replySeq) {

        List<ResponseReplyLikeRecord> resultList = replyLikeService.getAllReplyLikeByPostSeq(replySeq);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

}
