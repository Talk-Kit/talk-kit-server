package com.canal.post.service;

import com.canal.client.UserServiceClient;
import com.canal.post.domain.PostEntity;
import com.canal.post.domain.PostLikeEntity;
import com.canal.post.dto.RequestAddPostLike;
import com.canal.post.dto.ResponsePostLikeRecord;
import com.canal.post.repository.PostLikeRepository;
import com.canal.post.repository.PostRepository;
import com.canal.security.JwtFilter;
import com.canal.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    // 게시글 좋아요
    public ResponseEntity<?> createPostLike(@RequestBody RequestAddPostLike requestAddPostLike, Long postSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // 좋아요 존재 여부 확인
            PostLikeEntity postLikeEntity = postLikeRepository.findByPostSeqAndUserSeq(postSeq, userSeq);
            if(postLikeEntity == null || postLikeEntity.isDeleted()){
                // entity 저장
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                postLikeEntity = modelMapper.map(requestAddPostLike, PostLikeEntity.class);
                postLikeEntity.setUserSeq(userSeq);
                postLikeEntity.setPostSeq(postSeq);
                postLikeRepository.save(postLikeEntity);

                // post 테이블 post_like_num update
                PostEntity postEntity =  postRepository.findByPostSeq(postSeq);
                postEntity.setPostLikeNum(postEntity.getPostLikeNum()+1);
                postRepository.save(postEntity);

                return ResponseEntity.status(HttpStatus.OK).body("게시물 좋아요 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 좋아요 실패");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 좋아요 실패");
        }
    }

    // 게시글 좋아요 취소
    public ResponseEntity<?> deletePostLike(Long likeSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // 좋아요 존재 여부 확인
            PostLikeEntity postLikeEntity = postLikeRepository.findByLikeSeqAndUserSeq(likeSeq, userSeq);
            if(postLikeEntity != null && !postLikeEntity.isDeleted()){
                postLikeEntity.deletePostLike();
                postLikeRepository.save(postLikeEntity);

                // post 테이블 post_like_num update
                PostEntity postEntity =  postRepository.findByPostSeq(postLikeEntity.getPostSeq());
                postEntity.setPostLikeNum(postEntity.getPostLikeNum()-1);
                postRepository.save(postEntity);

                return ResponseEntity.status(HttpStatus.OK).body("게시물 좋아요 취소 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 좋아요 취소 실패");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 좋아요 취소 실패");
        }
    }

    // 모든 게시글 삭제되지 않은 좋아요 가져오기
    public List<ResponsePostLikeRecord> getAllPostLike() {
        List<PostLikeEntity> posts = postLikeRepository.findAll();
        List<ResponsePostLikeRecord> likeList = new ArrayList<>();
        posts.forEach(like -> {
            if(!like.isDeleted()){
                likeList.add(new ResponsePostLikeRecord(like));
            }
        });

        return likeList;
    }

    // 게시글 별 삭제되지 않은 좋아요 가져오기
    public List<ResponsePostLikeRecord> getAllPostLikeByPostSeq(Long postSeq) {
        List<PostLikeEntity> posts = postLikeRepository.findByPostSeq(postSeq);
        List<ResponsePostLikeRecord> likeList = new ArrayList<>();
        posts.forEach(like -> {
            if(!like.isDeleted()){
                likeList.add(new ResponsePostLikeRecord(like));
            }
        });

        return likeList;
    }

}
