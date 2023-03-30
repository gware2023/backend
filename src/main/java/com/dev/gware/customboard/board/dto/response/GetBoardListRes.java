package com.dev.gware.customboard.board.dto.response;

import com.dev.gware.customboard.board.domain.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GetBoardListRes {
    List<Board> boardList;
}
