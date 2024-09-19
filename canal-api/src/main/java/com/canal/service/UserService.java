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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder pwdEncoder, ModelMapper modelMapper,
                       AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.pwdEncoder = pwdEncoder;
        this.modelMapper = modelMapper;
    }

    public String authenticateAndGenerateToken(RequestLoginRecord requestLoginRecord){

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestLoginRecord.userId(), requestLoginRecord.userPwd()));
        return jwtUtil.generateToken(authentication.getName());
    }

    public boolean createUser(RequestJoin requestJoin) {

        try{
            System.out.println(requestJoin);
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
            throw new UsernameNotFoundException("User not found");
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

}
