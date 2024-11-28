package com.gulbalasalamov.taskmanagementsystem.repository;

import com.gulbalasalamov.taskmanagementsystem.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);
    List<Comment> findByUserId(Long userId);

}
