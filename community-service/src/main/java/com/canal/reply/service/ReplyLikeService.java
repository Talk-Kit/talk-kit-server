package com.canal.reply.service;

import com.canal.client.UserServiceClient;
import com.canal.reply.domain.ReplyEntity;
import com.canal.reply.domain.ReplyLikeEntity;
import com.canal.reply.dto.RequestAddReplyLike;
import com.canal.reply.dto.ResponseReplyLikeRecord;
import com.canal.reply.repository.ReplyLikeRepository;
import com.canal.reply.repository.ReplyRepository;
import com.canal.security.JwtFilter;
import com.canal.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyLikeService {
    private final ReplyLikeRepository replyLikeRepository;
    private final ReplyRepository replyRepository;
    private final ReplyRepository postRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    // 댓글 좋아요
    public boolean createReplyLike(@RequestBody RequestAddReplyLike requestAddReplyLike, @PathVariable Long replySeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);

            // 댓글인지 대댓글인지 확인(댓글만 좋아요 가능)
            ReplyEntity replyEntity = replyRepository.findByReplySeq(replySeq);
            if(replyEntity.getParentReplySeq() == null){
                // 좋아요 존재 여부 확인
                ReplyLikeEntity replyLikeEntity = replyLikeRepository.findByReplySeqAndUserSeq(replySeq, userSeq);
                if(replyLikeEntity == null || replyLikeEntity.isDeleted()){
                    // entity 저장
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                    replyLikeEntity = modelMapper.map(requestAddReplyLike, ReplyLikeEntity.class);
                    replyLikeEntity.setUserSeq(userSeq);
                    replyLikeEntity.setReplySeq(replySeq);
                    replyLikeRepository.save(replyLikeEntity);

                    // reply 테이블 reply_like_num update
                    ReplyEntity reply =  replyRepository.findByReplySeq(replySeq);
                    reply.setReplyLikeNum(reply.getReplyLikeNum()+1);
                    postRepository.save(reply);

                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 댓글 좋아요 취소
    public ReplyLikeEntity deleteReplyLike(Long likeSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // 좋아요 존재 여부 확인
            ReplyLikeEntity replyLikeEntity = replyLikeRepository.findByLikeSeqAndUserSeq(likeSeq, userSeq);
            if(replyLikeEntity != null && !replyLikeEntity.isDeleted()){
                replyLikeEntity.setDeleted(true);
                replyLikeEntity.setUpdatedAt(LocalDateTime.now());
                replyLikeRepository.save(replyLikeEntity);

                // reply 테이블 reply_like_num update
                ReplyEntity replyEntity =  replyRepository.findByReplySeq(replyLikeEntity.getReplySeq());
                replyEntity.setReplyLikeNum(replyEntity.getReplyLikeNum()-1);
                postRepository.save(replyEntity);

                return replyLikeEntity;
            }
            else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    // 모든 댓글 삭제되지 않은 좋아요 가져오기
    public List<ResponseReplyLikeRecord> getAllReplyLike() {
        List<ReplyLikeEntity> replys = replyLikeRepository.findAll();
        List<ResponseReplyLikeRecord> likeList = new ArrayList<>();
        replys.forEach(like -> {
            if(!like.isDeleted()){
                likeList.add(new ResponseReplyLikeRecord(like));
            }
        });

        return likeList;
    }

    // 댓글 별 삭제되지 않은 좋아요 가져오기
    public List<ResponseReplyLikeRecord> getAllReplyLikeByPostSeq(Long replySeq) {
        List<ReplyLikeEntity> replys = replyLikeRepository.findByReplySeq(replySeq);
        List<ResponseReplyLikeRecord> likeList = new ArrayList<>();
        replys.forEach(like -> {
            if(!like.isDeleted()){
                likeList.add(new ResponseReplyLikeRecord(like));
            }
        });

        return likeList;
    }

}
