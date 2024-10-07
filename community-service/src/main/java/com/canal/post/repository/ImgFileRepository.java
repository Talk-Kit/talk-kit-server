package com.canal.post.repository;

import com.canal.post.domain.ImgFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgFileRepository extends JpaRepository<ImgFileEntity, Long> {
    List<ImgFileEntity> findByPostSeqAndDeleted(Long postSeq, boolean deleted);
}
