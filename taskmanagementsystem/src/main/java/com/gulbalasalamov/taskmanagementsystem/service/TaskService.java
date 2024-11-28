package com.gulbalasalamov.taskmanagementsystem.service;

import com.gulbalasalamov.taskmanagementsystem.exception.TaskNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.mapper.TaskMapper;
import com.gulbalasalamov.taskmanagementsystem.repository.UserRepository;
import com.gulbalasalamov.taskmanagementsystem.repository.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.gulbalasalamov.taskmanagementsystem.model.dto.TaskDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskStatus;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskPriority;
import com.gulbalasalamov.taskmanagementsystem.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Page<TaskDTO> getFilteredTasks(String title,
                                          TaskStatus status,
                                          TaskPriority priority,
                                          Long authorId,
                                          Long assigneeId,
                                          int page,
                                          int size) {
        logger.info("Filtering tasks with parameters: title={}, status={}, priority={}, authorId={}, assigneeId={}, page={}, size={}",
                title, status, priority, authorId, assigneeId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Specification<Task> spec = TaskSpecification.withFilters(title, status, priority, authorId, assigneeId);
        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        logger.info("Retrieved {} tasks with filters", tasks.getTotalElements());
        return tasks.map(TaskMapper::toTaskDTO);
    }

    public List<TaskDTO> getAllTasks() {
        logger.info("Retrieving all tasks");
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = tasks.stream().map(TaskMapper::toTaskDTO).collect(Collectors.toList());
        logger.info("Retrieved {} tasks", taskDTOs.size());
        return taskDTOs;
    }

    public TaskDTO getTaskById(Long id) {
        logger.info("Retrieving task with ID: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> {
            logger.error("Task with ID: {} not found", id);
            return new RuntimeException("Task not found");
        });
        logger.info("Retrieved task with ID: {}", id);
        return TaskMapper.toTaskDTO(task);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        logger.info("Creating new task with title: {}", taskDTO.getTitle());
        validateTaskDTO(taskDTO);
        Task task = TaskMapper.toTask(taskDTO);
        task = taskRepository.save(task);
        logger.info("Task created successfully with ID: {}", task.getId());
        return TaskMapper.toTaskDTO(task);
    }

    /**
     * Task update process: Authorizations are determined according to the user role.
     * Users with role type - admin can update all fields.
     * Users with role type - user can only update the status of tasks assigned to them.
     * @param id            The id of the task to be updated
     * @param taskDTO       Data to be updated
     * @param userDetails   The user logged in
     * @return              Updated TaskDTO
     */
    public TaskDTO updateTask(Long id, TaskDTO taskDTO, UserDetails userDetails) {
        logger.info("Updating task with ID: {}", id);
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> {
            logger.error("Task with ID: {} not found", id);
            return new RuntimeException("Task not found");
        });

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isUser = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

        //Role Based Access Control (RBAC)
        if (isAdmin) {
            applyPartialUpdates(existingTask, taskDTO);
        } else if (isUser) {
            if (existingTask.getAssignee() != null &&
                    existingTask.getAssignee().getEmail().equals(userDetails.getUsername())) {
                if (taskDTO.getStatus() != null) {
                    existingTask.setTaskStatus(TaskStatus.valueOf(taskDTO.getStatus()));
                } else {
                    throw new RuntimeException("Users can only update the status of their assigned tasks");
                }
            } else {
                throw new RuntimeException("You are not allowed to update this task");
            }
        } else {
            throw new RuntimeException("Unauthorized role");
        }

        existingTask = taskRepository.save(existingTask);
        logger.info("Task updated successfully with ID: {}", id);

        return TaskMapper.toTaskDTO(existingTask);
    }

    public String deleteTask(Long id) {
        logger.info("Deleting task with ID: {}", id);
        if (!taskRepository.existsById(id)) {
            logger.error("Task with ID: {} not found", id);
            throw new TaskNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
        logger.info("Task with ID: {} deleted successfully", id);
        return "Task with id: " + id + " deleted successfully";

    }

    private void validateTaskDTO(TaskDTO taskDTO) {
        logger.info("Validating task DTO with title: {}", taskDTO.getTitle());
        if (taskDTO.getTitle() == null || taskDTO.getTitle().isEmpty()) {
            logger.error("Title cannot be null or empty");
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        // Additional validations can be added as needed
        logger.info("Task DTO with title: {} is valid", taskDTO.getTitle());
    }

    private void applyPartialUpdates(Task existingTask, TaskDTO taskDTO) {
        logger.info("Applying partial updates to task with ID: {}", existingTask.getId());
        if (taskDTO.getTitle() != null && !taskDTO.getTitle().isEmpty()) {
            existingTask.setTitle(taskDTO.getTitle());
        }
        if (taskDTO.getDescription() != null) {
            existingTask.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getStatus() != null) {
            existingTask.setTaskStatus(TaskStatus.valueOf(taskDTO.getStatus()));
        }
        if (taskDTO.getPriority() != null) {
            existingTask.setTaskPriority(TaskPriority.valueOf(taskDTO.getPriority()));
        }
        if (taskDTO.getAssigneeId() != null) {
            User assignee = new User();
            assignee.setId(taskDTO.getAssigneeId());
            existingTask.setAssignee(assignee);
        }
        logger.info("Partial updates applied to task with ID: {}", existingTask.getId());
    }

    public TaskDTO assignTaskToUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (task.getTaskStatus() == TaskStatus.COMPLETED) {
            throw new RuntimeException("Cannot assign a completed task.");
        }

        task.setAssignee(user);
        Task updatedTask = taskRepository.save(task);

        return TaskMapper.toTaskDTO(updatedTask);
    }

}
