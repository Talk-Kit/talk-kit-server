package com.canal.post.service;

import com.canal.client.UserServiceClient;
import com.canal.post.domain.PostEntity;
import com.canal.post.domain.PostReportEntity;
import com.canal.post.dto.RequestAddPostReport;
import com.canal.post.dto.RequestChangePostReport;
import com.canal.post.dto.ResponsePostReportRecord;
import com.canal.post.repository.PostReportRepository;
import com.canal.post.repository.PostRepository;
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
public class PostReportService {
    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    // 게시글 신고 작성
    public boolean createPostReport(RequestAddPostReport requestAddPostReport, Long postSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // 신고 존재 여부 확인
            PostReportEntity postReport = postReportRepository.findByReportSeqAndReportUserSeq(postSeq, userSeq);
            if(postReport == null || postReport.isDeleted()){
                PostEntity postReported = postRepository.findByPostSeq(postSeq);
                Long reportedUser = postReported.getUserSeq();

                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                PostReportEntity postReportEntity = modelMapper.map(requestAddPostReport, PostReportEntity.class);
                postReportEntity.setReportUserSeq(userSeq);
                postReportEntity.setReportedUserSeq(reportedUser);
                postReportEntity.setPostSeq(postSeq);
                postReportRepository.save(postReportEntity);

                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 게시글 신고 수정
    public boolean updatePostReport(RequestChangePostReport requestChangePostReport, Long reportSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // 신고 존재 여부 확인
            PostReportEntity postReportEntity = postReportRepository.findByReportSeqAndReportUserSeq(reportSeq, userSeq);
            if(postReportEntity != null || !postReportEntity.isDeleted()){
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                PostReportEntity changePostReportEntity = modelMapper.map(requestChangePostReport, PostReportEntity.class);

                postReportEntity.setReportReason(changePostReportEntity.getReportReason());
                postReportEntity.setUpdatedAt(LocalDateTime.now());
                postReportRepository.save(postReportEntity);
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 게시글 신고 삭제
    public PostReportEntity deletePostReport(Long reportSeq, HttpServletRequest httpServletRequest){
        try{
            //userId 추출
            String token = jwtFilter.resolveToken(httpServletRequest);
            String userId = jwtUtil.getUserIdFromJwt(token);
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeqByUserId(userId);
            // 신고 존재 여부 확인
            PostReportEntity postReportEntity = postReportRepository.findByReportSeqAndReportUserSeq(reportSeq, userSeq);
            if(postReportEntity != null || !postReportEntity.isDeleted()){
                postReportEntity.setDeleted(true);
                postReportEntity.setUpdatedAt(LocalDateTime.now());
                postReportRepository.save(postReportEntity);
                return postReportEntity;
            }
            else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    // 삭제되지 않은 게시글 신고 내역 가져오기
    public List<ResponsePostReportRecord> getAllPostReport() {
        List<PostReportEntity> posts = postReportRepository.findAll();
        List<ResponsePostReportRecord> reportList = new ArrayList<>();
        posts.forEach(report -> {
            if(!report.isDeleted()){
                reportList.add(new ResponsePostReportRecord(report));
            }
        });

        return reportList;
    }

    // 게시글 별 삭제되지 않은 게시글 신고 내역 가져오기
    public List<ResponsePostReportRecord> getAllPostReportByPostSeq(Long postSeq) {
        List<PostReportEntity> posts = postReportRepository.findByPostSeq(postSeq);
        List<ResponsePostReportRecord> reportList = new ArrayList<>();
        posts.forEach(report -> {
            if(!report.isDeleted()){
                reportList.add(new ResponsePostReportRecord(report));
            }
        });

        return reportList;
    }

}
