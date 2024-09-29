package com.canal.post.controller;

import com.canal.post.domain.PostLikeEntity;
import com.canal.post.dto.RequestAddPostLike;
import com.canal.post.dto.ResponsePostLikeRecord;
import com.canal.post.service.PostLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community-service/post/like")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 게시글 좋아요
    @PostMapping("/new/{postSeq}")
    public ResponseEntity<?> addPostLike(@RequestBody RequestAddPostLike requestAddPostLike, @PathVariable Long postSeq, HttpServletRequest httpServletRequest) {
        return postLikeService.createPostLike(requestAddPostLike, postSeq, httpServletRequest);
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/delete/{likeSeq}")
    public ResponseEntity<?> deletePostLike(@PathVariable Long likeSeq, HttpServletRequest httpServletRequest) {
        return postLikeService.deletePostLike(likeSeq, httpServletRequest);
    }

    // 모든 게시글 삭제되지 않은 좋아요 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<ResponsePostLikeRecord>> getAllPostLike() {

        List<ResponsePostLikeRecord> resultList = postLikeService.getAllPostLike();

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    // 게시글 별 삭제되지 않은 좋아요 가져오기
    @GetMapping("/list/{postSeq}")
    public ResponseEntity<List<ResponsePostLikeRecord>> getAllPostLikeByPostSeq(@PathVariable Long postSeq) {

        List<ResponsePostLikeRecord> resultList = postLikeService.getAllPostLikeByPostSeq(postSeq);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

}