package com.canal.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq",updatable = false)
    private Long userSeq;

    @Column(name = "user_id", nullable = false,unique = true)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Column(name = "user_email", nullable = false)
    @Email
    private String userEmail;

    @Column(name = "user_nickname", nullable = false,unique = true)
    private String userNickname;

    @Column(name = "user_affiliation", nullable = false)
    private String userAffiliation;

    @Column(name= "user_deleted",nullable = false)
    private boolean deleted = Boolean.FALSE;

    @Column(name= "terms_of_agreement",nullable = false)
    private boolean termsOfAgreement = Boolean.FALSE;

    @Column(name= "personal_info_agreement",nullable = false)
    private boolean personalInfoAgreement = Boolean.FALSE;

    @Column(name= "marketing_agreement",nullable = false)
    private boolean marketingAgreement = Boolean.FALSE;

}
