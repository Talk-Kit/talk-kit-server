package com.canal.post.repository;

import com.canal.post.domain.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
    PostLikeEntity findByLikeSeq(Long likeSeq);
    List<PostLikeEntity> findByPostSeq(Long postSeq);
    PostLikeEntity findByPostSeqAndUserSeq(Long postSeq, Long userSeq);
    PostLikeEntity findByLikeSeqAndUserSeq(Long likeSeq, Long userSeq);
}
