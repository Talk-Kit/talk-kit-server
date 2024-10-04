package com.canal.service;

import com.canal.domain.UserEntity;
import com.canal.domain.UserRepository;
import com.canal.dto.*;
import com.canal.security.JwtFilter;
import com.canal.security.JwtUtil;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService  {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder pwdEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final JavaMailSender javaMailSender;  // 의존성 주입을 통해 필요한 객체를 가져옴
    private final RedisService redisService;

    private static final int  AUTORIZATION_START_INDEX  = 7;
    private final JwtFilter jwtFilter;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public String authenticateAndGenerateToken(RequestLoginRecord requestLoginRecord){
        UserEntity user = userRepository.findByUserId(requestLoginRecord.userId());
        if (user == null) {
            throw new IllegalArgumentException("user null");
        }
        if (!pwdEncoder.matches(requestLoginRecord.userPwd(), user.getUserPwd())){
            throw new IllegalArgumentException("invalid userPwd or userId");
        }

        return jwtUtil.generateToken(requestLoginRecord.userId());
    }

    public boolean createUser(RequestJoin requestJoin) {

        try{
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            UserEntity userEntity = modelMapper.map(requestJoin, UserEntity.class);
            userEntity.setUserPwd(pwdEncoder.encode(requestJoin.getUserPwd()));

            userRepository.save(userEntity);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public ResponseUsersRecord getUserByJwt(HttpServletRequest request) {
        String token = jwtFilter.resolveToken(request);
        String userId = jwtUtil.getUserIdFromJwt(token);
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found");
        }
        return new ResponseUsersRecord(userEntity);
    }

    public List<ResponseUsersRecord> getAllUsers() {
        Iterable<UserEntity> users = userRepository.findAll();
        List<ResponseUsersRecord> userList = new ArrayList<>();
        users.forEach(user -> {
            userList.add(new ResponseUsersRecord(user));
        });

        return userList;
    }

    // 이메일 존재 여부 확인
    public ResponseEntity<?> existUserEmail(RequestMailCheck requestMailCheck) {
        try{
            if(requestMailCheck.getUserEmail() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 값 전달");
            }
            UserEntity user = userRepository.findByUserEmail(requestMailCheck.getUserEmail());
            if(user == null || user.isDeleted()){
                return ResponseEntity.status(HttpStatus.OK).body("이메일 없음");
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body("이메일 존재");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 아이디 존재 여부 확인
    public ResponseEntity<?> existUserId(RequestUserIdCheck requestUserIdCheck) {
        try{
            if(requestUserIdCheck.getUserId() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 값 전달");
            }

            UserEntity user = userRepository.findByUserId(requestUserIdCheck.getUserId());
            if(user == null || user.isDeleted()){
                return ResponseEntity.status(HttpStatus.OK).body("아이디 없음");
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body("아이디 존재");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 닉네임 존재 여부 확인
    public ResponseEntity<?> existUserNickname(RequestUserNicknameCheck requestUserNicknameCheck) {
        try {
            if(requestUserNicknameCheck.getUserNickname() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 값 전달");
            }

            UserEntity user = userRepository.findByUserNickname(requestUserNicknameCheck.getUserNickname());
            if(user == null || user.isDeleted()){
                return ResponseEntity.status(HttpStatus.OK).body("닉네임 없음");
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body("닉네임 존재");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 랜덤 인증 코드 생성
    public String createNumber() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <=57 || i >=65) && (i <= 90 || i>= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // 메일 양식 작성
    public MimeMessage createMail(String mail){
        String authCode = createNumber();  // 인증 코드 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);   // 보내는 이메일
            message.setRecipients(MimeMessage.RecipientType.TO, mail); // 보낼 이메일 설정
            message.setSubject("[Talk-kit] 회원가입을 위한 이메일 인증");  // 제목 설정
            String body = "";
            body += "<h1>" + "안녕하세요." + "</h1>";
            body += "<h1>" + "Talk-kit 입니다." + "</h1>";
            body += "<h3>" + "회원가입을 위한 요청하신 인증 번호입니다." + "</h3><br>";
            body += "<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>";

            body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
            body += "<h2>" + "회원가입 인증 코드입니다." + "</h2>";
            body += "<h1 style='color:blue'>" + authCode + "</h1>";
            body += "</div><br>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");

            redisService.setDataExpire(mail, authCode, 60 * 30L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    // 실제 메일 전송
    public ResponseEntity<?> sendEmail(RequestMailCheck requestMailCheck) {
        try {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            UserEntity userEntity = modelMapper.map(requestMailCheck, UserEntity.class);
            if(userEntity.getUserEmail() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 값 전달");
            }

            if (redisService.existData(requestMailCheck.getUserEmail())) {
                redisService.deleteData(requestMailCheck.getUserEmail());
            }

            // 메일 전송에 필요한 정보 설정
            MimeMessage message = createMail(requestMailCheck.getUserEmail());
            // 실제 메일 전송
            javaMailSender.send(message);

            return ResponseEntity.status(HttpStatus.OK).body("인증 번호 발송 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 메일 인증 코드 확인
    public ResponseEntity<?> verifyEmailCode(RequestMailCodeCheck requestMailCodeCheck) {
        try{
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            UserEntity userEntity = modelMapper.map(requestMailCodeCheck, UserEntity.class);
            if(userEntity.getUserEmail() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 값 전달");
            }

            String codeFoundByEmail = redisService.getData(requestMailCodeCheck.getUserEmail());
            if (codeFoundByEmail == null) {
                return ResponseEntity.status(HttpStatus.OK).body("이메일 인증 실패");
            }

            if(codeFoundByEmail.equals(requestMailCodeCheck.getAuthCode())){
                makeMemberId(requestMailCodeCheck.getUserEmail());
                return ResponseEntity.status(HttpStatus.OK).body("이메일 인증 성공");
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body("이메일 인증 실패");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public String makeMemberId(String mail) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(mail.getBytes());
            md.update(LocalDateTime.now().toString().getBytes());
            return Arrays.toString(md.digest());
        }
        catch (Exception e){
            return null;
        }
    }
    //for feign client
    public Long getUserSeq(String auth) {
        String resolvedAuth = auth.substring(AUTORIZATION_START_INDEX);
        String userId = jwtUtil.getUserIdFromJwt(resolvedAuth);
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found");
        }
        return userEntity.getUserSeq();
    }

}