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

import com.gulbalasalamov.taskmanagementsystem.exception.TaskNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.model.dto.TaskDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskStatus;
import com.gulbalasalamov.taskmanagementsystem.repository.TaskRepository;
import com.gulbalasalamov.taskmanagementsystem.repository.UserRepository;
import com.gulbalasalamov.taskmanagementsystem.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User user;
    private TaskDTO taskDTO;
    private UserDetails adminUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = createTask(1L, "New Task", TaskStatus.COMPLETED);
        user = createUser(2L, "testUser", "testUser@gmail.com");
        taskDTO = createTaskDTO(1L, "New Task", TaskStatus.COMPLETED.name());
        adminUser = createAdminUser();
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

    private TaskDTO createTaskDTO(Long id, String title, String status) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(id);
        taskDTO.setTitle(title);
        taskDTO.setStatus(status);
        return taskDTO;
    }

    private UserDetails createAdminUser() {
        return org.springframework.security.core.userdetails.User.withUsername("admin")
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();
    }

    @Test
    void shouldGetFilteredTasks() {
        Page<Task> taskPage = new PageImpl<>(List.of(task));
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(taskPage);

        Page<TaskDTO> result = taskService.getFilteredTasks("Task", null, null, null, null, 0, 10);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("New Task");
        assertThat(result.getContent().get(0).getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void shouldGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskDTO> result = taskService.getAllTasks();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("New Task");
        assertThat(result.get(0).getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void shouldGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO result = taskService.getTaskById(1L);

        assertThat(result.getTitle()).isEqualTo("New Task");
        assertThat(result.getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void shouldCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.createTask(taskDTO);

        assertThat(result.getTitle()).isEqualTo("New Task");
        assertThat(result.getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void shouldUpdateTaskAsAdmin() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.updateTask(1L, taskDTO, adminUser);

        assertThat(result.getTitle()).isEqualTo("New Task");
        assertThat(result.getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void shouldDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        String result = taskService.deleteTask(1L);

        assertThat(result).isEqualTo("Task with id: 1 deleted successfully");
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void shouldAssignTaskToUser() {
        task.setTaskStatus(TaskStatus.PENDING);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.assignTaskToUser(1L, 2L);

        assertThat(result.getAssigneeId()).isEqualTo(2L);
    }
}
