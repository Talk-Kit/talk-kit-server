package com.canal.post.service;

import com.canal.client.UserServiceClient;
import com.canal.post.domain.PostEntity;
import com.canal.post.domain.PostReportEntity;
import com.canal.post.dto.RequestAddPostReport;
import com.canal.post.dto.RequestChangePostReport;
import com.canal.post.dto.ResponsePostReportRecord;
import com.canal.post.repository.PostReportRepository;
import com.canal.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostReportService {
    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    // 게시글 신고 작성
    public ResponseEntity<?> createPostReport(RequestAddPostReport requestAddPostReport, Long postSeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // 신고 존재 여부 확인
            PostReportEntity postReport = postReportRepository.findByReportSeqAndReportUserSeq(postSeq, userSeq);
            if(postReport == null || postReport.isDeleted()){
                PostEntity postReported = postRepository.findByPostSeq(postSeq);
                Long reportedUser = postReported.getUserSeq();

                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                PostReportEntity postReportEntity = modelMapper.map(requestAddPostReport, PostReportEntity.class);
                postReportEntity.createPostReport(userSeq, reportedUser, postSeq);
                postReportRepository.save(postReportEntity);

                return ResponseEntity.status(HttpStatus.OK).body("게시물 신고 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 신고 실패");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 신고 실패");
        }
    }

    // 게시글 신고 수정
    public ResponseEntity<?> updatePostReport(RequestChangePostReport requestChangePostReport,Long reportSeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // 신고 존재 여부 확인
            PostReportEntity postReportEntity = postReportRepository.findByReportSeqAndReportUserSeq(reportSeq, userSeq);
            if(postReportEntity != null || !postReportEntity.isDeleted()){
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                PostReportEntity changePostReportEntity = modelMapper.map(requestChangePostReport, PostReportEntity.class);

                postReportEntity.updatePostReport(changePostReportEntity.getReportReason());
                postReportRepository.save(postReportEntity);
                return ResponseEntity.status(HttpStatus.OK).body("게시물 신고 수정 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 신소 수정 실패");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 신소 수정 실패");
        }
    }

    // 게시글 신고 삭제
    public ResponseEntity<?> deletePostReport(Long reportSeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // 신고 존재 여부 확인
            PostReportEntity postReportEntity = postReportRepository.findByReportSeqAndReportUserSeq(reportSeq, userSeq);
            if(postReportEntity != null || !postReportEntity.isDeleted()){
                postReportEntity.deletePostReport();
                postReportRepository.save(postReportEntity);
                return ResponseEntity.status(HttpStatus.OK).body("게시물 신고 삭제 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 신고 삭제 실패");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 신고 삭제 실패\"");
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
