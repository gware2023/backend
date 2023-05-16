package com.dev.gware.department.service;

import com.dev.gware.common.exception.ResourceNotFoundException;
import com.dev.gware.department.domain.UserDepartment;
import com.dev.gware.department.dto.UserDepartmentDTO;
import com.dev.gware.department.repository.UserDepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class UserDepartmentService {

    private final UserDepartmentRepository userDepartmentRepository;

    @Autowired
    public UserDepartmentService(UserDepartmentRepository userDepartmentRepository) {
        this.userDepartmentRepository = userDepartmentRepository;
    }

    public UserDepartmentDTO create(UserDepartmentDTO userDepartmentDTO) {
        // Convert DTO to Entity, save and return the created UserDepartment
        // TODO: Implement this method
        return null;
    }

    public UserDepartmentDTO findById(Long id) {
        UserDepartment userDepartment = userDepartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserDepartment not found with id: " + id));
        // Convert to DTO and return
        // TODO: Implement conversion to DTO
        return null;
    }

    public UserDepartmentDTO update(Long id, UserDepartmentDTO userDepartmentDTO) {
        // Find the existing UserDepartment, update its fields, save and return the updated UserDepartment
        // TODO: Implement this method
        return null;
    }

    public void delete(Long id) {
        if (!userDepartmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("UserDepartment not found with id: " + id);
        }
        userDepartmentRepository.deleteById(id);
    }

    public int getDepartmentDepth(Long id) {
        int depth = 0;
        Optional<UserDepartment> currentDepartment = userDepartmentRepository.findById(id);

        while(currentDepartment.isPresent() && currentDepartment.get().getDepartmentParents() != null) {
            depth++;
            currentDepartment = userDepartmentRepository.findById(currentDepartment.get().getDepartmentParents());
        }

        if(!currentDepartment.isPresent()) {
            throw new ResourceNotFoundException("UserDepartment not found with id: " + id);
        }

        return depth;
    }

}
