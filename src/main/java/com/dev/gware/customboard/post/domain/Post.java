package com.dev.gware.customboard.post.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "POST")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    long postId;

    @NonNull
    @Column(name = "BOARD_ID")
    long boardId;

    @NonNull
    @Column(name = "TITLE")
    String title;

    @NonNull
    @Column(name = "CONTENT")
    String content;

    @NonNull
    @Column(name = "USER_ID")
    long userId;

    @Column(name = "CREATE_DATETIME")
    String createDatetime;

    @Column(name = "MODIFY_DATETIME")
    String modifyDatetime;

    @NonNull
    @Column(name = "VIEW_COUNT")
    long viewCount;

    @NonNull
    @Column(name = "RECOMMENDATION_COUNT")
    long recommendationCount;
}
