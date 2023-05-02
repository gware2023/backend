package com.dev.gware.message.dto.response;

import com.dev.gware.message.domain.Message;
import com.dev.gware.user.dto.response.GetUserRes;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetMessageRes {

    private GetUserRes sender;

    private String title;

    private String content;

    private LocalDateTime sendDate;

    public GetMessageRes(Message message) {
        this.sender = new GetUserRes(message.getSender());
        this.title = message.getTitle();
        this.content = message.getContent();
        this.sendDate = message.getCreatedDate();
    }
}
