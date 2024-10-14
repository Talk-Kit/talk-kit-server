package com.canal.post.service;

import com.canal.client.UserServiceClient;
import com.canal.post.domain.PostEntity;
import com.canal.post.domain.PostLikeEntity;
import com.canal.post.dto.ResponsePostLikeRecord;
import com.canal.post.repository.PostLikeRepository;
import com.canal.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserServiceClient userServiceClient;

    // 게시글 좋아요
    public ResponseEntity<Boolean> likePost(Long postSeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // 좋아요 존재 여부 확인
            PostLikeEntity postLikeEntity = postLikeRepository.findByPostSeqAndUserSeq(postSeq, userSeq);
            // 좋아요 생성
            if(postLikeEntity == null || postLikeEntity.isDeleted()){
                postLikeEntity = new PostLikeEntity();
                // entity 저장
                postLikeEntity.setUserSeq(userSeq);
                postLikeEntity.setPostSeq(postSeq);
                postLikeRepository.save(postLikeEntity);

                // post 테이블 post_like_num update
                PostEntity postEntity =  postRepository.findByPostSeq(postSeq);
                postEntity.setPostLikeNum(postEntity.getPostLikeNum()+1);
                postRepository.save(postEntity);

                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
            // 좋아요 취소
            else{
                postLikeEntity.deletePostLike();
                postLikeRepository.save(postLikeEntity);

                // post 테이블 post_like_num update
                PostEntity postEntity =  postRepository.findByPostSeq(postLikeEntity.getPostSeq());
                postEntity.setPostLikeNum(postEntity.getPostLikeNum()-1);
                postRepository.save(postEntity);

                return ResponseEntity.status(HttpStatus.OK).body(false);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    // 게시글 별 삭제되지 않은 좋아요 가져오기
    public int getAllPostLikeByPostSeq(Long postSeq) {
        try{
            List<PostLikeEntity> posts = postLikeRepository.findByPostSeq(postSeq);
            List<ResponsePostLikeRecord> likeList = new ArrayList<>();
            posts.forEach(like -> {
                if(!like.isDeleted()){
                    likeList.add(new ResponsePostLikeRecord(like));
                }
            });

            System.out.println(likeList.size());
            return likeList.size();
        }
        catch (Exception e){
            return 0;
        }
    }

}
