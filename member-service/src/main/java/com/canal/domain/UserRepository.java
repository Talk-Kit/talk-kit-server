package com.canal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserSeq(Long userSeq);
    UserEntity findByUserId(String userId);
}
