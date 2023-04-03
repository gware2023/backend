package com.dev.gware.customboard.post.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "SURVEY")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SURVEY_ID")
    long surveyId;

    @NonNull
    @Column(name = "TITLE")
    String title;

    @Column(name = "REGISTRATION_TIME")
    String registrationTime;

    @NonNull
    @Column(name = "POST_ID")
    long postId;
}
