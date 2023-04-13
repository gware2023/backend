package com.dev.gware.customboard.board.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetBoardListRes {
    long boardId;
    String name;
    String createDatetime;
}
