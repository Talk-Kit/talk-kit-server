package com.canal.service;

import com.canal.domain.UserEntity;
import com.canal.domain.UserRepository;
import com.canal.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder pwdEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder pwdEncoder) {
        this.userRepository = userRepository;
        this.pwdEncoder = pwdEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UsernameNotFoundException(userId + ": not found");

        return new User(userEntity.getUserId(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        System.out.println(userDto.getUserPwd());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(pwdEncoder.encode(userDto.getUserPwd()));
        System.out.println(userEntity.getEncryptedPwd());
        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserSeq(Long userSeq) {
        UserEntity userEntity = userRepository.findByUserSeq(userSeq);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(userEntity, UserDto.class);
    }
}
