package com.canal.reply.service;

import com.canal.client.UserServiceClient;
import com.canal.post.domain.PostEntity;
import com.canal.post.repository.PostRepository;
import com.canal.reply.domain.ReplyEntity;
import com.canal.reply.dto.RequestAddReply;
import com.canal.reply.dto.RequestChangeReply;
import com.canal.reply.dto.ResponseReplyRecord;
import com.canal.reply.repository.ReplyRepository;
import com.canal.security.JwtFilter;
import com.canal.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    // 댓글 작성
    public boolean createReply(RequestAddReply requestAddReply, Long postSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);

            PostEntity postEntity = postRepository.findByPostSeq(postSeq);

            if(!postEntity.isDeleted()){
                // entity 저장
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                ReplyEntity replyEntity = modelMapper.map(requestAddReply, ReplyEntity.class);
                replyEntity.setUserSeq(userSeq);
                replyEntity.setPostSeq(postSeq);
                replyEntity.setReplyLikeNum(0);
                replyEntity.setReplyDepth(0);

                // 댓글 순서 설정
                List<ReplyEntity> replyOrder = replyRepository.findByPostSeq(postSeq);
                int order = 0;
                if(replyOrder.isEmpty()){
                    replyEntity.setReplyOrder(order);
                }
                else{
                    order = replyOrder.size();
                    replyEntity.setReplyOrder(order);
                }

                replyRepository.save(replyEntity);
                return true;
            }
            else{
                return false;
            }

        }catch (Exception e){
            return false;
        }
    }

    // 대댓글 작성
    public boolean createReReply(RequestAddReply requestAddReply, Long postSeq, Long replySeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);

            PostEntity postEntity = postRepository.findByPostSeq(postSeq);
            if(!postEntity.isDeleted()) {
                // entity 저장
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                ReplyEntity replyEntity = modelMapper.map(requestAddReply, ReplyEntity.class);
                replyEntity.setUserSeq(userSeq);
                replyEntity.setPostSeq(postSeq);
                replyEntity.setParentReplySeq(replySeq);

                // 댓글 순서 설정
                ReplyEntity replyOrder = replyRepository.findByReplySeq(replySeq);
                replyEntity.setReplyOrder(replyOrder.getReplyOrder());

                // 댓글 깊이 설정
                List<ReplyEntity> replyDepth = replyRepository.findByPostSeqAndParentReplySeq(postSeq, replySeq);
                if (replyDepth.isEmpty()) {
                    replyEntity.setReplyDepth(1);
                } else {
                    replyEntity.setReplyDepth(replyDepth.size() + 1);
                }

                replyRepository.save(replyEntity);

                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 댓글, 대댓글 수정
    public boolean updateReply(RequestChangeReply requestChangeReply, Long replySeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // userSeq 값 일치 여부 확인
            ReplyEntity replyEntity = replyRepository.findByReplySeqAndUserSeq(replySeq, userSeq);
            if(replyEntity != null  || !replyEntity.isDeleted()){
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                ReplyEntity changeReplyEntity = modelMapper.map(requestChangeReply, ReplyEntity.class);

                replyEntity.setReplyContent(changeReplyEntity.getReplyContent());
                replyEntity.setUpdatedAt(LocalDateTime.now());

                replyRepository.save(replyEntity);

                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 댓글, 대댓글 삭제
    public boolean deleteReply(Long replySeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // userSeq 값 일치 여부 확인
            ReplyEntity replyEntity = replyRepository.findByReplySeqAndUserSeq(replySeq, userSeq);
            if(replyEntity != null && !replyEntity.isDeleted()){
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                replyEntity.setDeleted(true);
                replyEntity.setUpdatedAt(LocalDateTime.now());
                replyRepository.save(replyEntity);

                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return true;
        }
    }

    // 삭제되지 않은 모든 댓글, 대댓글 가져오기
    public List<ResponseReplyRecord> getAllReply() {
        List<ReplyEntity> posts = replyRepository.findAll();
        List<ResponseReplyRecord> userList = new ArrayList<>();
        posts.forEach(reply -> {
            if(!reply.isDeleted()){
                userList.add(new ResponseReplyRecord(reply));
            }
        });

        return userList;
    }

    // 게시글 별 삭제되지 않은 모든 댓글, 대댓글 가져오기
    public List<ResponseReplyRecord> getAllReplyByPostSeq(Long postSeq) {
        List<ReplyEntity> replys = replyRepository.findByPostSeqOrderByReplyOrderAscReplyDepth(postSeq);
        List<ResponseReplyRecord> userList = new ArrayList<>();
        replys.forEach(reply -> {
            if(!reply.isDeleted()){
                userList.add(new ResponseReplyRecord(reply));
            }
        });

        return userList;
    }

}
