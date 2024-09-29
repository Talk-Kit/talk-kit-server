package com.canal.post.controller;

import com.canal.post.dto.RequestAddPost;
import com.canal.post.dto.RequestChangePost;
import com.canal.post.dto.ResponsePostRecord;
import com.canal.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community-service/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/new")
    public ResponseEntity<?> addPost(@RequestBody RequestAddPost requestAddPost, HttpServletRequest httpServletRequest) {
        return postService.createPost(requestAddPost,httpServletRequest);
    }

    // 게시글 수정
    @PutMapping("/update/{postSeq}")
    public ResponseEntity<?> updatePost(@RequestBody RequestChangePost requestChangePost, @PathVariable Long postSeq, HttpServletRequest httpServletRequest) {
        return postService.updatePost(requestChangePost, postSeq, httpServletRequest);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{postSeq}")
    public ResponseEntity<?> deletePost(@PathVariable Long postSeq, HttpServletRequest httpServletRequest) {
        return postService.delete(postSeq, httpServletRequest);
    }

    // 삭제되지 않은 모든 게시글 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<ResponsePostRecord>> getAllPost() {

        List<ResponsePostRecord> resultList = postService.getAllPost();

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    // 게시판 유형 별 삭제되지 않은 모든 게시글 가져오기
    @GetMapping("/list/{postType}")
    public ResponseEntity<List<ResponsePostRecord>> getAllPostByPostType(@PathVariable int postType) {

        List<ResponsePostRecord> resultList = postService.getAllPostByPostType(postType);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    // 게시판 유형 별 삭제되지 않은 모든 공개 게시글 가져오기
    @GetMapping("/list/public/{postType}")
    public ResponseEntity<List<ResponsePostRecord>> getAllPublicPostByPostType(@PathVariable int postType) {

        List<ResponsePostRecord> resultList = postService.getAllPublicPostByPostType(postType);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    // 키워드 검색
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ResponsePostRecord>> getKeywordPost(@PathVariable String keyword) {

        List<ResponsePostRecord> resultList = postService.getKeywordPost(keyword);

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

}
