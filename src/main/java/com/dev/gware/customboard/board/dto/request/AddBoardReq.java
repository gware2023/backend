package com.dev.gware.customboard.board.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddBoardReq {
    @NotBlank
    String name;
}
