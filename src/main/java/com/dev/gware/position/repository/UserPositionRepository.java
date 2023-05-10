package com.dev.gware.position.repository;

import com.dev.gware.position.domain.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserPositionRepository extends CrudRepository<UserPosition, Long> {
}
