package com.dev.gware.message.service;

import com.dev.gware.message.dto.request.SendMessageReq;
import com.dev.gware.message.dto.response.GetMessageRes;

public interface MessageService {

    void send(SendMessageReq sendMessageReq);

    GetMessageRes findMessage(Long messageId, Long userId);
}
