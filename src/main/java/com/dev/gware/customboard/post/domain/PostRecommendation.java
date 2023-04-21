package com.dev.gware.customboard.post.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_RECOMMENDATION_ID")
    long postRecommendationId;

    @NonNull
    @Column(name = "POST_ID")
    long postId;

    @NonNull
    @Column(name = "USER_ID")
    long userId;

    @Column(name = "RECOMMEND_DATETIME")
    String recommendDatetime;
}
