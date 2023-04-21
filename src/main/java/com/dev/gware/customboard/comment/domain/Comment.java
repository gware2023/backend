package com.dev.gware.customboard.comment.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    long commentId;

    @NonNull
    @Column(name = "CONTENT")
    String content;

    @Column(name = "CREATE_DATETIME")
    String createDatetime;

    @NonNull
    @Column(name = "POST_ID")
    long postId;

    @NonNull
    @Column(name = "USER_ID")
    long userId;

    @NonNull
    @Column(name = "USER_NAME")
    String userName;
}
