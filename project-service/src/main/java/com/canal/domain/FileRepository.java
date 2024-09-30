package com.canal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Iterable<FileEntity> findByProjectSeqAndDeleted(Long projectSeq,boolean deleted);
    FileEntity findByFileSeqAndDeleted(Long fileSeq,boolean deleted);
    List<FileEntity> findAllByDeletedAndUpdatedAtBefore(boolean deleted, LocalDateTime updatedAt);
}
