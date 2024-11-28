package com.gulbalasalamov.taskmanagementsystem;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.gulbalasalamov.taskmanagementsystem.exception.CommentNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.exception.TaskNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.model.dto.CommentDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Comment;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskStatus;
import com.gulbalasalamov.taskmanagementsystem.model.mapper.CommentMapper;
import com.gulbalasalamov.taskmanagementsystem.repository.CommentRepository;
import com.gulbalasalamov.taskmanagementsystem.repository.TaskRepository;
import com.gulbalasalamov.taskmanagementsystem.repository.UserRepository;
import com.gulbalasalamov.taskmanagementsystem.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.AccessDeniedException;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private Task task;
    private User user;
    private CommentDTO commentDTO;
    private UserDetails adminUser;
    private UserDetails regularUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = createTask(1L, "Task 1", TaskStatus.PENDING);
        user = createUser(2L, "testUser", "testUser@gmail.com");
        comment = createComment(1L, "This is a comment", task, user);
        commentDTO = createCommentDTO(comment);
        adminUser = createAdminUser();
        regularUser = createRegularUser(user);

        User admin = new User(3L, "adminUser", "admin", "password", new Date(), new Date(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        when(userRepository.findByEmail("admin")).thenReturn(Optional.of(admin));

    }

    private Task createTask(Long id, String title, TaskStatus status) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setTaskStatus(status);
        task.setAssignee(user);
        return task;
    }

    private User createUser(Long id, String username, String email) {
        return new User(id, username, email, "password", new Date(), new Date(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private Comment createComment(Long id, String content, Task task, User user) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        comment.setTask(task);
        comment.setUser(user);
        comment.setCreatedAt(new Date());
        return comment;
    }

    private CommentDTO createCommentDTO(Comment comment) {
        return CommentMapper.toCommentDTO(comment);
    }

    private UserDetails createAdminUser() {
        return org.springframework.security.core.userdetails.User.withUsername("admin")
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();
    }

    private UserDetails createRegularUser(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }

    @Test
    void shouldAddComment() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO result = commentService.addComment(task.getId(), user.getId(), commentDTO, adminUser);

        assertThat(result.getContent()).isEqualTo("This is a comment");
    }

    @Test
    void shouldThrowExceptionWhenAddingCommentToNonExistentTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> commentService.addComment(task.getId(), user.getId(), commentDTO, adminUser));
    }

    @Test
    void shouldUpdateCommentAsAdmin() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentDTO.setContent("Updated content");
        CommentDTO result = commentService.updateComment(comment.getId(), commentDTO, adminUser);

        assertThat(result.getContent()).isEqualTo("Updated content");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentComment() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(comment.getId(), commentDTO, adminUser));
    }

    @Test
    void shouldDeleteCommentAsAdmin() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        commentService.deleteComment(comment.getId(), adminUser);

        verify(commentRepository).delete(comment);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentComment() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(comment.getId(), adminUser));
    }

    @Test
    void shouldGetCommentsByTask() {
        when(commentRepository.findByTaskId(task.getId())).thenReturn(List.of(comment));

        List<CommentDTO> result = commentService.getCommentsByTask(task.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("This is a comment");
    }

    @Test
    void shouldGetCommentsByUser() {
        when(commentRepository.findByUserId(user.getId())).thenReturn(List.of(comment));

        List<CommentDTO> result = commentService.getCommentsByUser(user.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("This is a comment");
    }
}
