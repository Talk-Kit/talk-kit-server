package com.canal.post.dto;

import com.canal.post.domain.PostEntity;

public record ResponsePostRecord(
        Long postSeq,
        String postTitle,
        int postType,
        boolean deleted,
        int postLikeNum,
        String postContent,
        String postScope,
        Long userSeq
){
    public ResponsePostRecord(PostEntity postEntity) {
        this(postEntity.getPostSeq(),postEntity.getPostTitle(), postEntity.getPostType(), postEntity.isDeleted(),
                postEntity.getPostLikeNum(), postEntity.getPostContent(), postEntity.getPostScope(),
                postEntity.getUserSeq());
    }
}
