package com.dev.gware.department.repository;

import com.dev.gware.department.domain.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.CrudRepository;

public interface UserDepartmentRepository extends CrudRepository<UserDepartment, Long> {
}
