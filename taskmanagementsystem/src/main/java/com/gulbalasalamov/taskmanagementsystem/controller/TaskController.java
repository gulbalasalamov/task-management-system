package com.gulbalasalamov.taskmanagementsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.gulbalasalamov.taskmanagementsystem.model.dto.TaskDTO;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskStatus;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskPriority;
import com.gulbalasalamov.taskmanagementsystem.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id,
                                              @RequestBody TaskDTO taskDTO,
                                              Authentication authentication) {
        logger.info("Updating task with id: {}", id);
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        TaskDTO updatedTaskDTO = taskService.updateTask(id, taskDTO, currentUser);
        logger.info("Task updated successfully with id: {}", id);
        return new ResponseEntity<>(updatedTaskDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("assign/{id}")
    public ResponseEntity<TaskDTO> assignTaskToUser(@PathVariable Long id,
                                                    @RequestParam Long userId) {
        TaskDTO assignedTask = taskService.assignTaskToUser(id,userId);
        return new ResponseEntity<>(assignedTask, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TaskDTO>> getFilteredTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) TaskStatus taskStatus,
            @RequestParam(required = false) TaskPriority taskPriority,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TaskDTO> taskPage = taskService.getFilteredTasks(title, taskStatus, taskPriority, authorId, assigneeId, page, size);
        return new ResponseEntity<>(taskPage, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTaskDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        String responseMessage = taskService.deleteTask(id);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
