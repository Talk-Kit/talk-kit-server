package com.canal.post.repository;

import com.canal.post.domain.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository  extends JpaRepository<PostEntity, Long> {
    PostEntity findByPostSeq(Long postSeq);
    PostEntity findByPostSeqAndUserSeq(Long postSeq, Long userSeq);
    List<PostEntity> findByPostType(int postType);
    @Override
    PostEntity save(PostEntity post);
}
