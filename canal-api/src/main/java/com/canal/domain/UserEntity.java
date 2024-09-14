package com.canal.domain;

import com.domain.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq",updatable = false)
    private Long userSeq;

    @Column(name = "user_id", nullable = false,unique = true)
    private String userId;

    @Column(name = "user_pw", nullable = false)
    private String encryptedPwd;

    @Column(name = "user_email", nullable = false)
    @Email
    private String userEmail;

    @Column(name = "user_nickname", nullable = false,unique = true)
    private String userNickname;

    @Column(name = "user_affiliation", nullable = false)
    private String userAffiliation;

}
