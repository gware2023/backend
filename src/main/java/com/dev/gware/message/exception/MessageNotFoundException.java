package com.dev.gware.message.exception;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException() {
        super("쪽지를 찾을 수 없습니다.");
    }

    public MessageNotFoundException(String msg) {
        super(msg);
    }
}
