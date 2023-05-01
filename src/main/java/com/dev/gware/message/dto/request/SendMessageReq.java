package com.dev.gware.message.dto.request;

import com.dev.gware.message.domain.Message;
import com.dev.gware.message.domain.MessageStatus;
import com.dev.gware.user.domain.Users;
import lombok.Getter;

@Getter
public class SendMessageReq {

    private Long senderId;

    private Long receiverId;

    private String title;

    private String content;

    public Message createMessage(Users sender, Users receiver) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setStatus(MessageStatus.SEND);
        message.setTitle(this.title);
        message.setContent(this.content);
        return message;
    }
}
