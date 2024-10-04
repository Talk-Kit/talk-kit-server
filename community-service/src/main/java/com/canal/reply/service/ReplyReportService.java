package com.canal.reply.service;

import com.canal.client.UserServiceClient;
import com.canal.reply.domain.ReplyEntity;
import com.canal.reply.domain.ReplyReportEntity;
import com.canal.reply.dto.RequestAddReplyReport;
import com.canal.reply.dto.RequestChangeReplyReport;
import com.canal.reply.dto.ResponseReplyReportRecord;
import com.canal.reply.repository.ReplyReportRepository;
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
public class ReplyReportService {
    private final ReplyReportRepository replyReportRepository;
    private final ReplyRepository replyRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    // 댓글, 대댓글 신고 작성
    public boolean createReplyReport(RequestAddReplyReport requestAddReplyReport, Long replySeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // 신고 존재 여부 확인
            ReplyReportEntity replyReport = replyReportRepository.findByReportSeqAndReportUserSeq(replySeq, userSeq);
            if(replyReport == null || replyReport.isDeleted()){
                ReplyEntity replyReported = replyRepository.findByReplySeq(replySeq);
                Long reportedUser = replyReported.getUserSeq();

                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                ReplyReportEntity postReportEntity = modelMapper.map(requestAddReplyReport, ReplyReportEntity.class);
                postReportEntity.setReportUserSeq(userSeq);
                postReportEntity.setReportedUserSeq(reportedUser);
                postReportEntity.setReplySeq(replySeq);
                replyReportRepository.save(postReportEntity);

                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 댓글, 대댓글 신고 수정
    public boolean updateReplyReport(RequestChangeReplyReport requestChangeReplyReport, Long reportSeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // 신고 존재 여부 확인
            ReplyReportEntity replyReportEntity = replyReportRepository.findByReportSeqAndReportUserSeq(reportSeq, userSeq);
            if(replyReportEntity != null && !replyReportEntity.isDeleted()){
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                ReplyReportEntity changeReplyReportEntity = modelMapper.map(requestChangeReplyReport, ReplyReportEntity.class);

                replyReportEntity.setReportReason(changeReplyReportEntity.getReportReason());
                replyReportEntity.setUpdatedAt(LocalDateTime.now());
                replyReportRepository.save(replyReportEntity);
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 댓글, 대댓글 신고 삭제
    public ReplyReportEntity deleteReplyReport(Long reportSeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // 신고 존재 여부 확인
            ReplyReportEntity replyReportEntity = replyReportRepository.findByReportSeqAndReportUserSeq(reportSeq, userSeq);
            if(replyReportEntity != null && !replyReportEntity.isDeleted()){
                replyReportEntity.setDeleted(true);
                replyReportEntity.setUpdatedAt(LocalDateTime.now());
                replyReportRepository.save(replyReportEntity);
                return replyReportEntity;
            }
            else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    // 삭제되지 않은 댓글, 대댓글 신고 내역 가져오기
    public List<ResponseReplyReportRecord> getAllReplyReport() {
        List<ReplyReportEntity> posts = replyReportRepository.findAll();
        List<ResponseReplyReportRecord> reportList = new ArrayList<>();
        posts.forEach(report -> {
            if(!report.isDeleted()){
                reportList.add(new ResponseReplyReportRecord(report));
            }
        });

        return reportList;
    }

    // 댓글 별 삭제되지 않은 댓글, 대댓글 신고 내역 가져오기
    public List<ResponseReplyReportRecord> getAllReplyReportByPostSeq(Long replySeq) {
        List<ReplyReportEntity> posts = replyReportRepository.findByReplySeq(replySeq);
        List<ResponseReplyReportRecord> reportList = new ArrayList<>();
        posts.forEach(report -> {
            if(!report.isDeleted()){
                reportList.add(new ResponseReplyReportRecord(report));
            }
        });

        return reportList;
    }

}
