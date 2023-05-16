package com.dev.gware.department.controller;

import com.dev.gware.department.dto.UserDepartmentDTO;
import com.dev.gware.department.service.UserDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-departments")
public class UserDepartmentController {

    private final UserDepartmentService userDepartmentService;

    @Autowired
    public UserDepartmentController(UserDepartmentService userDepartmentService) {
        this.userDepartmentService = userDepartmentService;
    }

    @PostMapping
    public UserDepartmentDTO createUserDepartment(@RequestBody UserDepartmentDTO userDepartmentDTO) {
        return userDepartmentService.create(userDepartmentDTO);
    }

    @GetMapping("/{id}")
    public UserDepartmentDTO getUserDepartment(@PathVariable Long id) {
        return userDepartmentService.findById(id);
    }

    @PutMapping("/{id}")
    public UserDepartmentDTO updateUserDepartment(@PathVariable Long id, @RequestBody UserDepartmentDTO userDepartmentDTO) {
        return userDepartmentService.update(id, userDepartmentDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUserDepartment(@PathVariable Long id) {
        userDepartmentService.delete(id);
    }

    @GetMapping("/{id}/depth")
    public int getDepartmentDepth(@PathVariable Long id) {
        return userDepartmentService.getDepartmentDepth(id);
    }
}
