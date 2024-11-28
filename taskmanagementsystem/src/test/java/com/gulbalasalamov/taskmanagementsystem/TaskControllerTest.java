package com.gulbalasalamov.taskmanagementsystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulbalasalamov.taskmanagementsystem.controller.TaskController;
import com.gulbalasalamov.taskmanagementsystem.model.dto.TaskDTO;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskPriority;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskStatus;
import com.gulbalasalamov.taskmanagementsystem.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private TaskDTO taskDTO;
    private Page<TaskDTO> taskPage;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Test Task");
        taskDTO.setStatus(TaskStatus.PENDING.name());
        taskDTO.setPriority(TaskPriority.HIGH.name());
        taskDTO.setAssigneeId(2L);
        taskDTO.setAssigneeId(3L);

        taskPage = new PageImpl<>(List.of(taskDTO));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateTask() throws Exception {
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        mockMvc.perform(post("/api/v1/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(taskDTO))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService).createTask(any(TaskDTO.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteTask() throws Exception {
        when(taskService.deleteTask(anyLong())).thenReturn("Task deleted successfully");

        mockMvc.perform(delete("/api/v1/task/delete/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));

        verify(taskService).deleteTask(1L);
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void shouldUpdateTask() throws Exception {
        when(taskService.updateTask(anyLong(), any(TaskDTO.class), any(UserDetails.class)))
                .thenReturn(taskDTO);

        mockMvc.perform(patch("/api/v1/task/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(taskDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService).updateTask(eq(1L), any(TaskDTO.class), any(UserDetails.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAssignTaskToUser() throws Exception {
        when(taskService.assignTaskToUser(anyLong(), anyLong())).thenReturn(taskDTO);

        mockMvc.perform(patch("/api/v1/task/assign/{id}", 1L)
                        .param("userId", "3")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assigneeId").value(3L));

        verify(taskService).assignTaskToUser(1L, 3L);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldGetFilteredTasks() throws Exception {
        when(taskService.getFilteredTasks(any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(taskPage);

        mockMvc.perform(get("/api/v1/task/filter")
                        .param("title", "Test")
                        .param("taskStatus", "PENDING")
                        .param("taskPriority", "HIGH")
                        .param("authorId", "2")
                        .param("assigneeId", "3")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Task"));

        verify(taskService).getFilteredTasks("Test", TaskStatus.PENDING, TaskPriority.HIGH, 2L, 3L, 0, 10);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(taskDTO));

        mockMvc.perform(get("/api/v1/task/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));

        verify(taskService).getAllTasks();
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void shouldGetTaskById() throws Exception {
        when(taskService.getTaskById(anyLong())).thenReturn(taskDTO);

        mockMvc.perform(get("/api/v1/task/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService).getTaskById(1L);
    }
}
