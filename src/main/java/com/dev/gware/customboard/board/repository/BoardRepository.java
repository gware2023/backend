package com.dev.gware.customboard.board.repository;

import com.dev.gware.customboard.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAll();

    Board findByBoardId(long boardId);

    @Transactional
    void deleteByBoardId(long boardId);

}
