package com.dev.gware.department.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "USER_DEPARTMENT")
public class UserDepartment {

    @Id
    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "DEPARTMENT_DESCRIPTION")
    private String departmentDescription;

    @Column(name = "DEPARTMENT_ORIGIN")
    private Integer departmentOrigin;

    @Column(name = "DEPARTMENT_PARENTS")
    private Integer departmentParents;

    @Column(name = "DEPARTMENT_ORDER")
    private Integer departmentOrder;

    @Column(name = "STOP_YN")
    private String stopYn;

    @Column(name = "CREATE_DATE_TIME")
    private LocalDateTime createDateTime;

    @Column(name = "CREATE_USER_ID")
    private Long createUserId;

    @Column(name = "MODIFY_DATE_TIME")
    private LocalDateTime modifyDateTime;

    @Column(name = "MODIFY_USER_ID")
    private Long modifyUserId;

public Long getDepartmentParents(){
        return (Long)this.getDepartmentParents();
    }
}
