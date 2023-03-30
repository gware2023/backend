package com.dev.gware.customboard.board.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BOARD")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    long boardId;

    @NonNull
    @Column(name = "NAME")
    String name;

    @Column(name = "TIME")
    String time;
}
