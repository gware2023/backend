package com.dev.gware.message.controller;

import com.dev.gware.common.response.CommonResponse;
import com.dev.gware.message.dto.request.SendMessageReq;
import com.dev.gware.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public CommonResponse sendMessage(@RequestBody SendMessageReq sendMessageReq) {
        messageService.send(sendMessageReq);
        return new CommonResponse(true, 200, "메시지 전송 성공");
    }

}
