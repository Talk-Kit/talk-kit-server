package com.canal.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.java.Log;

@Data
@Entity
@Table(name = "files")
public class FileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_seq",updatable = false)
    private Long fileSeq;

    @Column(name = "file_name", nullable = false)
    private String fileName = "새로운 파일";

    @Lob
    @Column(name = "file_content", columnDefinition = "LONGTEXT")
    private String fileContent;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name= "project_seq",nullable = false,updatable = false)
    private Long projectSeq;

    @Column(name= "file_deleted",nullable = false)
    private boolean deleted = Boolean.FALSE;
}
