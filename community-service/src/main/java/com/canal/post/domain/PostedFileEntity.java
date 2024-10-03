package com.canal.post.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "posted_file")
public class PostedFileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posted_file_seq", updatable = false)
    private Long postedFileSeq;

    @Column(name = "post_seq", nullable = false, updatable = false)
    private Long postSeq;

    @Column(name = "file_seq", nullable = false, updatable = false)
    private Long fileSeq;

    @Column(name= "posted_file_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

    public void deletePostedFile(){
        this.deleted = true;
        setUpdatedAt(LocalDateTime.now());
    }
}
