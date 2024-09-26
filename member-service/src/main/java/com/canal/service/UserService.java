package com.canal.service;

import com.canal.domain.UserEntity;
import com.canal.domain.UserRepository;
import com.canal.dto.RequestJoin;
import com.canal.dto.RequestLoginRecord;
import com.canal.dto.ResponseUsersRecord;
import com.canal.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService  {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder pwdEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder pwdEncoder, ModelMapper modelMapper,
                       JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.pwdEncoder = pwdEncoder;
        this.modelMapper = modelMapper;
    }

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

    public ResponseUsersRecord getUserByUserSeq(Long userSeq) {
        UserEntity userEntity = userRepository.findByUserSeq(userSeq);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found");
        }
        return new ResponseUsersRecord(userEntity);
    }
    public Long getUserSeqByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found");
        }
        return userEntity.getUserSeq();
    }

    public List<ResponseUsersRecord> getAllUsers() {
        Iterable<UserEntity> users = userRepository.findAll();
        List<ResponseUsersRecord> userList = new ArrayList<>();
        users.forEach(user -> {
            userList.add(new ResponseUsersRecord(user));
        });

        return userList;
    }

}