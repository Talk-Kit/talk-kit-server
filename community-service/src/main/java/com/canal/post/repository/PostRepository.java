package com.canal.post.repository;

import com.canal.post.domain.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository  extends JpaRepository<PostEntity, Long> {
    PostEntity findByPostSeq(Long postSeq);
    PostEntity findByPostSeqAndDeleted(Long postSeq, boolean deleted);
    PostEntity findByPostSeqAndUserSeq(Long postSeq, Long userSeq);
    List<PostEntity> findByPostTypeAndDeleted(int postType, boolean deleted);
    List<PostEntity> findTop5ByDeletedOrderByPostLikeNumDescUpdatedAtDesc(boolean deleted);
    @Override
    PostEntity save(PostEntity post);
    PostEntity deleteByPostSeq(Long postSeq);
}
