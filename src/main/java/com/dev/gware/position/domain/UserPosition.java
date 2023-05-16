package com.dev.gware.position.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "USER_POSITION")
public class UserPosition {

    @Id
    @Column(name = "POSITION_ID")
    private Long positionId;

    @Column(name = "POSITION_NAME")
    private String positionName;

    @Column(name = "POSITION_DESCRIPTION")
    private String positionDescription;

    @Column(name = "POSITION_ORIGIN")
    private Integer positionOrigin;

    @Column(name = "POSITION_PARENTS")
    private Integer positionParents;

    @Column(name = "POSITION_ORDER")
    private Integer positionOrder;

    @Column(name = "CREATE_DATETIME")
    private LocalDateTime createDateTime;

    @Column(name = "CREATE_USER_ID")
    private Long createUserId;

    @Column(name = "MODIFY_DATETIME")
    private LocalDateTime modifyDateTime;

    @Column(name = "MODIFY_USER_ID")
    private Long modifyUserId;

}
