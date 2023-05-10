package com.dev.gware.customboard.board.domain;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "BOARD")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    long boardId;

    @NonNull
    @Column(name = "NAME")
    String name;

    @NonNull
    @Column(name = "USER_ID")
    long userId;

    @Column(name = "CREATE_DATETIME")
    String createDatetime;
}
