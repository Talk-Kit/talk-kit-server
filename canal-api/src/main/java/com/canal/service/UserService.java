package com.canal.service;

import com.canal.domain.UserEntity;
import com.canal.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    // 사용자 생성
    UserDto createUser(UserDto userDto);

    // 개별 사용자 조회
    UserDto getUserByUserSeq(Long userSeq);

    // 전체 사용자 조회
    Iterable<UserEntity> getAllUsers();

    // 사용자 아이디로 상세조회
    UserDto getUserDetailsByUserId(String userId);

}
