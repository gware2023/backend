package com.dev.gware.message.service;

import com.dev.gware.message.dto.request.SendMessageReq;

public interface MessageService {

    void send(SendMessageReq sendMessageReq);
}
