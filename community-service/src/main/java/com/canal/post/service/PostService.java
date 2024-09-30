package com.canal.post.service;

import com.canal.client.UserServiceClient;
import com.canal.post.domain.PostEntity;
import com.canal.post.dto.RequestAddPost;
import com.canal.post.dto.RequestChangePost;
import com.canal.post.dto.ResponsePostRecord;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    // 게시글 작성
    public ResponseEntity<?> createPost(RequestAddPost requestAddPost, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // entity 저장
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            PostEntity postEntity = modelMapper.map(requestAddPost, PostEntity.class);
            postEntity.setUserSeq(userSeq);
            postRepository.save(postEntity);
            return ResponseEntity.status(HttpStatus.OK).body("게시물 생성 성공");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 생성 실패");
        }
    }

    // 게시글 수정
    public ResponseEntity<?> updatePost(RequestChangePost requestChangePost, Long postSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // userSeq 값 일치 여부 확인
            PostEntity postEntity = postRepository.findByPostSeqAndUserSeq(postSeq, userSeq);
            if(postEntity != null){
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                PostEntity changePostEntity = modelMapper.map(requestChangePost, PostEntity.class);

                postEntity.updatePost(changePostEntity.getPostTitle(), changePostEntity.getPostType(),
                        changePostEntity.getPostContent(), LocalDateTime.now());

                postRepository.save(postEntity);

                return ResponseEntity.status(HttpStatus.OK).body("게시물 수정 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 수정 실패");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 수정 실패");
        }
    }

    // 게시글 삭제
    public ResponseEntity<?> delete(Long postSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // userSeq 값 일치 여부 확인
            PostEntity postEntity = postRepository.findByPostSeqAndUserSeq(postSeq, userSeq);
            if(postEntity != null && !postEntity.isDeleted()){
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                postEntity.deletePost();
                postRepository.save(postEntity);

                return ResponseEntity.status(HttpStatus.OK).body("게시물 삭제 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 삭제 실패");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 삭제 실패");
        }
    }

    // 삭제되지 않은 모든 게시글 가져오기
    public List<ResponsePostRecord> getAllPost() {
        List<PostEntity> posts = postRepository.findAll();
        List<ResponsePostRecord> userList = new ArrayList<>();
        posts.forEach(post -> {
            if(!post.isDeleted()){
                userList.add(new ResponsePostRecord(post));
            }
        });

        return userList;
    }

    // 게시판 유형 별 삭제되지 않은 모든 게시글 가져오기
    public List<ResponsePostRecord> getAllPostByPostType(int postType) {
        List<PostEntity> posts = postRepository.findByPostType(postType);
        List<ResponsePostRecord> userList = new ArrayList<>();
        posts.forEach(post -> {
            if(!post.isDeleted()){
                userList.add(new ResponsePostRecord(post));
            }
        });

        return userList;
    }

    // 게시판 유형 별 삭제되지 않은 모든 공개 게시글 가져오기
    public List<ResponsePostRecord> getAllPublicPostByPostType(int postType) {
        List<PostEntity> posts = postRepository.findByPostType(postType);
        List<ResponsePostRecord> userList = new ArrayList<>();
        posts.forEach(post -> {
            if(!post.isDeleted() && post.getPostScope().equals("public")){
                userList.add(new ResponsePostRecord(post));
            }
        });

        return userList;
    }

    // 키워드 검색
    public List<ResponsePostRecord> getKeywordPost(String keyword) {
        List<PostEntity> posts = postRepository.findAll();
        List<ResponsePostRecord> userList = new ArrayList<>();
        posts.forEach(post -> {
            if(!post.isDeleted()){
                if(post.getPostContent().contains(keyword) || post.getPostTitle().contains(keyword)){
                    userList.add(new ResponsePostRecord(post));
                }
            }
        });

        return userList;
    }
}
