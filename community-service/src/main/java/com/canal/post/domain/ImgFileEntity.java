package com.canal.post.domain;

import com.canal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "img_file")
public class ImgFileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_seq", updatable = false)
    private Long fileSeq;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name= "post_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

    @Column(name = "post_seq", nullable = false, updatable = false)
    private Long postSeq;

    public void deletePost(){
        this.deleted = true;
        setUpdatedAt(LocalDateTime.now());
    }

}
