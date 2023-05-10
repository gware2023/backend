package com.dev.gware.department.dto;

import java.time.LocalDateTime;

public class UserDepartmentDTO {
    private Long departmentId;
    private String departmentName;
    private String departmentDescription;
    private Integer departmentOrigin;
    private Integer departmentParents;
    private Integer departmentOrder;
    private String stopYn;
    private LocalDateTime createDateTime;
    private Long createUserId;
    private LocalDateTime modifyDateTime;
    private Long modifyUserId;

}
