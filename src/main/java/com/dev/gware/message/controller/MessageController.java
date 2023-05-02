package com.dev.gware.message.controller;

import com.dev.gware.auth.domain.AuthUser;
import com.dev.gware.common.response.CommonResponse;
import com.dev.gware.message.dto.request.SendMessageReq;
import com.dev.gware.message.dto.response.GetMessageRes;
import com.dev.gware.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{messageId}")
    public CommonResponse<GetMessageRes> findMessage(@PathVariable("messageId") Long messageId,
                                                     @AuthenticationPrincipal AuthUser user) {

        return new CommonResponse<>(true, 200, "메시지 조회 성공", messageService.findMessage(messageId, user.getId()));
    }
}
