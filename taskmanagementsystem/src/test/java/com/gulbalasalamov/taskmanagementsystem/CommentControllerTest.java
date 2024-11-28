package com.gulbalasalamov.taskmanagementsystem;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.gulbalasalamov.taskmanagementsystem.controller.CommentController;
import com.gulbalasalamov.taskmanagementsystem.model.dto.CommentDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Comment;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskStatus;
import com.gulbalasalamov.taskmanagementsystem.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
//
//@WebMvcTest(CommentController.class)
//public class CommentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CommentService commentService;
//
//    private Comment comment;
//    private Task task;
//    private User user;
//    private CommentDTO commentDTO;
//    private UserDetails adminUser;
//    private UserDetails regularUser;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        task = createTask(1L, "Task 1", TaskStatus.PENDING);
//        user = createUser(2L, "testUser", "testUser@gmail.com");
//        comment = createComment(1L, "This is a comment", task, user);
//        commentDTO = createCommentDTO(comment);
//        adminUser = createAdminUser();
//        regularUser = createRegularUser(user);
//    }
//
//    private Task createTask(Long id, String title, TaskStatus status) {
//        Task task = new Task();
//        task.setId(id);
//        task.setTitle(title);
//        task.setTaskStatus(status);
//        task.setAssignee(user);
//        return task;
//    }
//
//    private User createUser(Long id, String username, String email) {
//        return new User(id, username, email, "password", new Date(), new Date(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//    }
//
//    private Comment createComment(Long id, String content, Task task, User user) {
//        Comment comment = new Comment();
//        comment.setId(id);
//        comment.setContent(content);
//        comment.setTask(task);
//        comment.setUser(user);
//        comment.setCreatedAt(new Date());
//        return comment;
//    }
//
//    private CommentDTO createCommentDTO(Comment comment) {
//        CommentDTO commentDTO = new CommentDTO();
//        commentDTO.setId(comment.getId());
//        commentDTO.setContent(comment.getContent());
//        commentDTO.setTaskId(comment.getTask().getId());
//        commentDTO.setUserId(comment.getUser().getId());
//        return commentDTO;
//    }
//
//    private UserDetails createAdminUser() {
//        return org.springframework.security.core.userdetails.User.withUsername("admin")
//                .password("password")
//                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
//                .build();
//    }
//
//    private UserDetails createRegularUser(User user) {
//        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
//                .password(user.getPassword())
//                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
//                .build();
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    void shouldAddComment() throws Exception {
//        when(commentService.addComment(task.getId(), user.getId(), commentDTO, adminUser)).thenReturn(commentDTO);
//
//        mockMvc.perform(post("/api/v1/comment/create")
//                        .param("taskId", task.getId().toString())
//                        .param("userId", user.getId().toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(commentDTO))
//                        .with(csrf()))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.content").value("This is a comment"));
//
//        verify(commentService).addComment(task.getId(), user.getId(), commentDTO, adminUser);
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    void shouldUpdateComment() throws Exception {
//        when(commentService.updateComment(comment.getId(), commentDTO, adminUser)).thenReturn(commentDTO);
//
//        commentDTO.setContent("Updated content");
//
//        mockMvc.perform(patch("/api/v1/comment/update/{commentId}", comment.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(commentDTO))
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").value("Updated content"));
//
//        verify(commentService).updateComment(comment.getId(), commentDTO, adminUser);
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    void shouldDeleteComment() throws Exception {
//        mockMvc.perform(delete("/api/v1/comment/delete/{commentId}", comment.getId())
//                        .with(csrf()))
//                .andExpect(status().isNoContent());
//
//        verify(commentService).deleteComment(comment.getId(), adminUser);
//    }
//
//    @Test
//    @WithMockUser(roles = {"USER", "ADMIN"})
//    void shouldGetCommentsByTask() throws Exception {
//        when(commentService.getCommentsByTask(task.getId())).thenReturn(List.of(commentDTO));
//
//        mockMvc.perform(get("/api/v1/comment/task/{taskId}", task.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].content").value("This is a comment"));
//
//        verify(commentService).getCommentsByTask(task.getId());
//    }
//
//    @Test
//    @WithMockUser(roles = {"USER", "ADMIN"})
//    void shouldGetCommentsByUser() throws Exception {
//        when(commentService.getCommentsByUser(user.getId())).thenReturn(List.of(commentDTO));
//
//        mockMvc.perform(get("/api/v1/comment/user/{userId}", user.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].content").value("This is a comment"));
//
//        verify(commentService).getCommentsByUser(user.getId());
//    }
//
//    private static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private Comment comment;
    private Task task;
    private User user;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        task = createTask(1L, "Task 1", TaskStatus.PENDING);
        user = createUser(2L, "testUser", "testUser@gmail.com");
        comment = createComment(1L, "This is a comment", task, user);
        commentDTO = createCommentDTO(comment);
    }

    private Task createTask(Long id, String title, TaskStatus status) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setTaskStatus(status);
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
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setTaskId(comment.getTask().getId());
        commentDTO.setUserId(comment.getUser().getId());
        return commentDTO;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldAddComment() throws Exception {
        when(commentService.addComment(anyLong(), anyLong(), any(CommentDTO.class), any(UserDetails.class)))
                .thenReturn(commentDTO);

        mockMvc.perform(post("/api/v1/comment/create")
                        .param("taskId", String.valueOf(task.getId()))
                        .param("userId", String.valueOf(user.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentDTO))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(comment.getContent()));

        verify(commentService).addComment(eq(task.getId()), eq(user.getId()), any(CommentDTO.class), any(UserDetails.class));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldUpdateComment() throws Exception {
        commentDTO.setContent("Updated content");
        when(commentService.updateComment(anyLong(), any(CommentDTO.class), any(UserDetails.class)))
                .thenReturn(commentDTO);

        mockMvc.perform(patch("/api/v1/comment/update/{commentId}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));

        verify(commentService).updateComment(eq(comment.getId()), any(CommentDTO.class), any(UserDetails.class));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldDeleteComment() throws Exception {
        doNothing().when(commentService).deleteComment(anyLong(), any(UserDetails.class));

        mockMvc.perform(delete("/api/v1/comment/delete/{commentId}", comment.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(commentService).deleteComment(eq(comment.getId()), any(UserDetails.class));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void shouldGetCommentsByTask() throws Exception {
        when(commentService.getCommentsByTask(anyLong())).thenReturn(List.of(commentDTO));

        mockMvc.perform(get("/api/v1/comment/task/{taskId}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(comment.getContent()));

        verify(commentService).getCommentsByTask(task.getId());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void shouldGetCommentsByUser() throws Exception {
        when(commentService.getCommentsByUser(anyLong())).thenReturn(List.of(commentDTO));

        mockMvc.perform(get("/api/v1/comment/user/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(comment.getContent()));

        verify(commentService).getCommentsByUser(user.getId());
    }
}
