package com.canal.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "projects")
public class ProjectEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_seq",updatable = false)
    private Long projectSeq;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name= "user_seq",nullable = false,updatable = false)
    private Long userSeq;

    @Column(name= "project_deleted",nullable = false)
    private boolean deleted = Boolean.FALSE;
}
