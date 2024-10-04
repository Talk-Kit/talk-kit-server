package com.canal.post.service;

import com.canal.client.NHNStorageClient;
import com.canal.client.UserServiceClient;
import com.canal.post.domain.ImgFileEntity;
import com.canal.post.domain.PostEntity;
import com.canal.post.domain.PostedFileEntity;
import com.canal.post.dto.RequestAddPost;
import com.canal.post.dto.RequestAddPostedFile;
import com.canal.post.dto.ResponsePostRecord;
import com.canal.post.repository.ImgFileRepository;
import com.canal.post.repository.PostRepository;
import com.canal.post.repository.PostedFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostedFileRepository postedFileRepository;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;
    private static final Map<String,String> contentTypeMap = new HashMap<>();
    private final NHNStorageClient nhnStorageClient;
    private final NHNAuthService nhnAuthService;
    @Value("${nhn.storage.url}")
    private String STORAGE_URL;
    private final ImgFileRepository imgFileRepository;

    static {
        // 이미지
        contentTypeMap.put("image/jpeg", ".jpeg");
        contentTypeMap.put("image/png", ".png");
    }

    // 게시글 작성
    @Transactional
    public ResponseEntity<?> createPost(RequestAddPost requestAddPost, MultipartFile[] file, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // entity 저장
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            PostEntity postEntity = modelMapper.map(requestAddPost, PostEntity.class);
            postEntity.setUserSeq(userSeq);
            PostEntity savedPost = postRepository.save(postEntity);

            // 게시된 파일 저장
            requestAddPost.getFiles().forEach(pfile -> {
                RequestAddPostedFile requestAddPostedFile = new RequestAddPostedFile();
                requestAddPostedFile.setPostSeq(savedPost.getPostSeq());
                requestAddPostedFile.setFileSeq(pfile);
                PostedFileEntity postedFileEntity = modelMapper.map(requestAddPostedFile, PostedFileEntity.class);
                postedFileRepository.save(postedFileEntity);
            });

            // 업로드 할 이미지 파일이 없는 경우
            if(file.length == 0){
                return ResponseEntity.status(HttpStatus.CREATED).body("게시물 생성 성공");
            }
            // nhn 토큰 발급
            String nhnToken = nhnAuthService.getNHNToken();
            if (nhnToken == null){ // 발급 실패시
                postRepository.deleteByPostSeq(savedPost.getPostSeq());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NHN 토큰 발급 실패");
            }
            for (MultipartFile fileSave : file){
                // 스토리지 업로드
                String storageUrl = uploadFile(fileSave, nhnToken, savedPost.getPostSeq());
                if (storageUrl == null){
                    postRepository.deleteByPostSeq(savedPost.getPostSeq());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("스토리지 업로드 실패: 요청값을 확인해주세요");
                }
                // 디비 저장
                boolean success = saveFiles(storageUrl, savedPost.getPostSeq(), fileSave.getOriginalFilename());
                if (!success){
                    postRepository.deleteByPostSeq(savedPost.getPostSeq());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("디비 저장 실패: 요청값을 확인해주세요");
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("게시물 생성, 파일 저장 성공");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 생성 실패");
        }
    }

    @Transactional
    public String uploadFile(MultipartFile file, String nhnToken, Long postSeq){
        try{
            // 스토리지 저장시 postSeq 폴더를 생성

            String folder = "p"+postSeq;
            String contentType = file.getContentType();
            String objectName = setRandomFileName(contentType);
            byte[] bytes = file.getBytes();
            ResponseEntity<Void> response = nhnStorageClient.uploadfile(
                    folder,
                    objectName,
                    nhnToken,
                    contentType,
                    bytes
            );
            if (response.getStatusCode().value() != 201){
                return null;
            }
            return STORAGE_URL+"/"+folder+"/"+objectName; // 스토리지 저장 성공시 url 반환

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private String setRandomFileName(String contentType){
        String extension = contentTypeMap.get(contentType);
        if (extension == null){
            extension = ".bin";
        }
        return UUID.randomUUID().toString() + extension;
    }

    @Transactional
    public boolean saveFiles(String storageUrl, Long postSeq, String fileName){
        try{
            ImgFileEntity imgFileEntity = new ImgFileEntity();
            imgFileEntity.setFileUrl(storageUrl);
            imgFileEntity.setPostSeq(postSeq);
            imgFileRepository.save(imgFileEntity);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    // 게시글 삭제
    public ResponseEntity<?> delete(Long postSeq, String auth){
        try{
            // userSeq 요청
            Long userSeq = userServiceClient.getUserSeq(auth);
            // userSeq 값 일치 여부 확인
            PostEntity postEntity = postRepository.findByPostSeqAndUserSeq(postSeq, userSeq);
            if(postEntity != null && !postEntity.isDeleted()){
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                postEntity.deletePost();
                postRepository.save(postEntity);

                List<PostedFileEntity> postedFileEntityList = postedFileRepository.findByPostSeqAndDeleted(postSeq, false);
                postedFileEntityList.forEach(files -> {
                    files.deletePostedFile();
                    postedFileRepository.save(files);
                });

                deleteFile(postSeq);

                return ResponseEntity.status(HttpStatus.OK).body("게시물 삭제 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 삭제 실패");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("게시물 삭제 실패");
        }
    }

    // 파일 삭제
    @Transactional
    public boolean deleteFile(Long postSeq){
        try{
            List<ImgFileEntity> imgFileEntity = imgFileRepository.findByPostSeqAndDeleted(postSeq,false);
            imgFileEntity.forEach(files -> {
                files.setDeleted(true);
                imgFileRepository.save(files);
            });

            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
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
