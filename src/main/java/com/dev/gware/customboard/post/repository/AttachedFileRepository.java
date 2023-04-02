package com.dev.gware.customboard.post.repository;

import com.dev.gware.customboard.post.domain.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachedFileRepository extends JpaRepository<AttachedFile, String> {
}
