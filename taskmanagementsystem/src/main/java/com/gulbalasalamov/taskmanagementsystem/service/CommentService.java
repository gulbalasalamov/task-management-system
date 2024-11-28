package com.gulbalasalamov.taskmanagementsystem.service;

import com.gulbalasalamov.taskmanagementsystem.exception.CommentNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.exception.TaskNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.model.dto.CommentDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Comment;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.mapper.CommentMapper;
import com.gulbalasalamov.taskmanagementsystem.repository.CommentRepository;
import com.gulbalasalamov.taskmanagementsystem.repository.TaskRepository;
import com.gulbalasalamov.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentDTO addComment(Long taskId, Long userId, CommentDTO commentDTO, UserDetails userDetails) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
        if (isAdmin || (isUser && task.getAssignee().getId().equals(userId))) {
            Comment comment = new Comment();
            comment.setTask(task);
            comment.setUser(user);
            comment.setContent(commentDTO.getContent());
            comment.setCreatedAt(new Date());
            commentRepository.save(comment);
            return CommentMapper.toCommentDTO(comment);
        } else {
            throw new AccessDeniedException("You do not have permission to comment on this task.");
        }
    }

    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO, UserDetails userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDetails.getUsername()));
        if (isAdmin || (isUser && comment.getUser().getId().equals(currentUser.getId()))) {
            comment.setContent(commentDTO.getContent());
            comment.setUpdatedAt(new Date());
            commentRepository.save(comment);
            return CommentMapper.toCommentDTO(comment);
        } else {
            throw new AccessDeniedException("You do not have permission to update this comment.");
        }
    }

    public void deleteComment(Long commentId, UserDetails userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDetails.getUsername()));
        if (isAdmin || (isUser && comment.getUser().getId().equals(currentUser.getId()))) {
            commentRepository.delete(comment);
        } else {
            throw new AccessDeniedException("You do not have permission to delete this comment.");
        }
    }

    public List<CommentDTO> getCommentsByTask(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream().map(CommentMapper::toCommentDTO).collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentsByUser(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream().map(CommentMapper::toCommentDTO).collect(Collectors.toList());
    }

}
