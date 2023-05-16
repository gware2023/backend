package com.dev.gware.position.controller;

import com.dev.gware.common.exception.ResourceNotFoundException;
import com.dev.gware.position.domain.UserPosition;
import com.dev.gware.position.dto.UserPositionDto;
import com.dev.gware.position.service.UserPositionService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/positions")
public class UserPositionController {

    private final UserPositionService userPositionService;

    @Autowired
    public UserPositionController(UserPositionService userPositionService) {
        this.userPositionService = userPositionService;
    }

    @GetMapping("/{id}")
    public UserPosition getPosition(@PathVariable Long id) {
        return userPositionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + id));
    }

    @PostMapping
    public UserPosition createPosition(@RequestBody UserPosition userPosition) {
        return userPositionService.save(userPosition);
    }

    @PutMapping("/{id}")
    public UserPositionDto updatePosition(@PathVariable Long id, @RequestBody UserPositionDto updatedUserPositionDTO) {
        return userPositionService.findById(id).map(userPosition -> {
            userPosition.setPositionName(updatedUserPositionDTO.getPositionName());
            userPosition.setPositionDescription(updatedUserPositionDTO.getPositionDescription());

            return UserPositionDto.fromEntity(userPositionService.save(userPosition));
        }).orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletePosition(@PathVariable Long id) {
        if (userPositionService.existsById(id)) {
            userPositionService.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Position not found with id: " + id);
        }
    }
}
