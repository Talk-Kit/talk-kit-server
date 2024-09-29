package com.canal.post.dto;

import com.canal.post.domain.PostLikeEntity;

public record ResponsePostLikeRecord(
        Long likeSeq,
        boolean deleted,
        Long userSeq,
        Long postSeq
){
    public ResponsePostLikeRecord(PostLikeEntity postLikeEntity) {
        this(postLikeEntity.getLikeSeq(), postLikeEntity.isDeleted(),
                postLikeEntity.getUserSeq(), postLikeEntity.getPostSeq());
    }
}
