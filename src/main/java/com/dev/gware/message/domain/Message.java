package com.dev.gware.message.domain;

import com.dev.gware.user.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity(name = "MESSAGES")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @JoinColumn(name = "SENDER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users sender;

    @JoinColumn(name = "RECEIVER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users receiver;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @CreatedDate
    @Column(name = "CREATE_DATETIME", updatable = false)
    private LocalDateTime createdDate;

    public Message(String title, String content, Users sender, Users receiver, MessageStatus status) {
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public void readMessage() {
        this.status = MessageStatus.READ;
    }

    public void deleteMessage() {
        this.status = MessageStatus.DELETE;
    }
}
