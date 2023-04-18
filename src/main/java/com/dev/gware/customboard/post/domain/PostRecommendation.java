package com.dev.gware.customboard.post.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "POST_RECOMMENDATION")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class PostRecommendation {
    @Id
    long dummyPK;

    @NonNull
    @Column(name = "POST_ID")
    long postId;

    @NonNull
    @Column(name = "USER_ID")
    long userId;

    @Column(name = "CREATE_DATETIME")
    String createDatetime;
}
