package com.canal.reply.service;

import com.canal.client.UserServiceClient;
import com.canal.reply.domain.ReplyEntity;
import com.canal.reply.domain.ReplyLikeEntity;
import com.canal.reply.dto.ResponseReplyLikeRecord;
import com.canal.reply.repository.ReplyLikeRepository;
import com.canal.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private final UserServiceClient userServiceClient;

    // 댓글 좋아요
    public ResponseEntity<Boolean> likeReply(Long replySeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);

            // 댓글인지 대댓글인지 확인(댓글만 좋아요 가능)
            ReplyEntity replyEntity = replyRepository.findByReplySeq(replySeq);
            if(replyEntity.getParentReplySeq() == null){
                // 좋아요 존재 여부 확인
                ReplyLikeEntity replyLikeEntity = replyLikeRepository.findByReplySeqAndUserSeq(replySeq, userSeq);
                if(replyLikeEntity == null || replyLikeEntity.isDeleted()){
                    // entity 저장
                    replyLikeEntity.setUserSeq(userSeq);
                    replyLikeEntity.setReplySeq(replySeq);
                    replyLikeRepository.save(replyLikeEntity);

                    // reply 테이블 reply_like_num update
                    ReplyEntity reply =  replyRepository.findByReplySeq(replySeq);
                    reply.setReplyLikeNum(reply.getReplyLikeNum()+1);
                    postRepository.save(reply);

                    return ResponseEntity.status(HttpStatus.OK).body(true);
                }
                else{
                    replyLikeEntity.setDeleted(true);
                    replyLikeEntity.setUpdatedAt(LocalDateTime.now());
                    replyLikeRepository.save(replyLikeEntity);

                    // reply 테이블 reply_like_num update
                    ReplyEntity reply =  replyRepository.findByReplySeq(replySeq);
                    reply.setReplyLikeNum(reply.getReplyLikeNum()-1);
                    postRepository.save(reply);

                    return ResponseEntity.status(HttpStatus.OK).body(false);
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    // 댓글 별 삭제되지 않은 좋아요 가져오기
    public int getAllReplyLikeByPostSeq(Long replySeq) {
        try{
            List<ReplyLikeEntity> replys = replyLikeRepository.findByReplySeq(replySeq);
            List<ResponseReplyLikeRecord> likeList = new ArrayList<>();
            replys.forEach(like -> {
                if(!like.isDeleted()){
                    likeList.add(new ResponseReplyLikeRecord(like));
                }
            });

            return likeList.size();
        }
        catch (Exception e){
            return 0;
        }
    }

}
