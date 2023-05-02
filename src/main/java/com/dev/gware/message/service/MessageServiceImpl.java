package com.dev.gware.message.service;

import com.dev.gware.message.domain.Message;
import com.dev.gware.message.dto.request.SendMessageReq;
import com.dev.gware.message.dto.response.GetMessageRes;
import com.dev.gware.message.exception.MessageNotFoundException;
import com.dev.gware.message.repository.MessageRepository;
import com.dev.gware.user.domain.Users;
import com.dev.gware.user.exception.UserNotFoundException;
import com.dev.gware.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    @Override
    public void send(final SendMessageReq sendMessageReq) {
        Optional<Users> senderOptional = userRepository.findById(sendMessageReq.getSenderId());
        Optional<Users> receiverOptional = userRepository.findById(sendMessageReq.getReceiverId());

        if (senderOptional.isEmpty()) {
            log.error("전송하는 유저를 찾을 수 없습니다. id: {}", sendMessageReq.getSenderId());
            throw new UserNotFoundException("쪽지 전송 대상을 찾을 수 없습니다.");
        }

        if (receiverOptional.isEmpty()) {
            log.error("쪽지 수신 대상을 찾울 수 없습니다. id: {}", sendMessageReq.getReceiverId());
            throw new UserNotFoundException("쪽지 수신 대상을 찾울 수 없습니다.");
        }
        Message message = sendMessageReq.createMessage(senderOptional.get(), receiverOptional.get());
        messageRepository.save(message);
    }

    @Override
    public GetMessageRes findMessage(Long messageId, Long userId) {
        Optional<Message> findMessageOptional = messageRepository.findById(messageId);
        if (findMessageOptional.isEmpty()) {
            throw new MessageNotFoundException();
        }
        Message findMessage = findMessageOptional.get();

        Long receiverId = findMessage.getReceiver().getId();
        if (userId != receiverId) {
            throw new MessageNotFoundException(); // 쪽지를 조회한 유저가 쪽지의 대상이 아니면 쪽지는 없는 것으로 처리
        }

        // TODO 삭제된 message의 경우도 처리 해야됨 / 어떻게 처리할지?
        findMessage.readMessage();
        messageRepository.save(findMessage);

        return new GetMessageRes(findMessage);
    }
}
