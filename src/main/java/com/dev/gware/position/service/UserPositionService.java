package com.dev.gware.position.service;

import com.dev.gware.position.domain.UserPosition;
import com.dev.gware.position.repository.UserPositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Service
public class UserPositionService {

    private final UserPositionRepository userPositionRepository;

    @Autowired
    public UserPositionService(UserPositionRepository userPositionRepository) {
        this.userPositionRepository = userPositionRepository;
    }

    public Optional<UserPosition> findById(Long id) {
        return userPositionRepository.findById(id);
    }

    public UserPosition save(UserPosition userPosition) {
        return userPositionRepository.save(userPosition);
    }

    public boolean existsById(Long id) { return userPositionRepository.existsById(id);
    }

    public void deleteById(Long id) { userPositionRepository.deleteById(id); }
}
