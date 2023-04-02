package com.dev.gware.customboard.post.repository;

import com.dev.gware.customboard.post.domain.ImgFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgFileRepository extends JpaRepository<ImgFile, String> {
}
