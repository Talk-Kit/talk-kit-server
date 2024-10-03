package com.canal.post.repository;

import com.canal.post.domain.PostedFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostedFileRepository extends JpaRepository<PostedFileEntity, Long> {
    PostedFileEntity findByFileSeqAndPostSeq(long fileSeq, long postSeq);
    List<PostedFileEntity> findByPostSeqAndDeleted(long postSeq, boolean deleted);
}
