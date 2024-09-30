package com.canal.reply.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestChangeReply {
    @NotNull
    private String replyContent;

}
