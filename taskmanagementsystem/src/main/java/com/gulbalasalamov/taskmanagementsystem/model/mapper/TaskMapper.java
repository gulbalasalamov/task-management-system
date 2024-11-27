package com.gulbalasalamov.taskmanagementsystem.model.mapper;

import com.gulbalasalamov.taskmanagementsystem.model.dto.CommentDTO;
import com.gulbalasalamov.taskmanagementsystem.model.dto.TaskDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Comment;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.enums.Priority;
import com.gulbalasalamov.taskmanagementsystem.model.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskDTO toTaskDTO(Task task) {
        if (task == null) {
            return null;
        }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus().name());
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setUpdatedAt(task.getUpdatedAt());
        taskDTO.setPriority(task.getPriority() != null ? task.getPriority().name() : null);

        taskDTO.setAssigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null);

        taskDTO.setCommentIds(task.getComments() != null ? task.getComments().stream()
                .map(Comment::getId)
                .collect(Collectors.toList())
                : new ArrayList<>());



        return taskDTO;
    }

    public static Task toTask(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }

        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCreatedAt(taskDTO.getCreatedAt());
        task.setUpdatedAt(taskDTO.getUpdatedAt());

        if (taskDTO.getStatus() != null) {
            try {
                task.setStatus(Status.valueOf(taskDTO.getStatus()));
            } catch (IllegalArgumentException e) {
                // TODO: For the wrong ENUM handle appropiately
                throw new RuntimeException("Invalid status value: " + taskDTO.getStatus());
            }
        }

        if (taskDTO.getPriority() != null) {
            task.setPriority(Priority.valueOf(taskDTO.getPriority()));
        }

        // Assignee conversion
        if (taskDTO.getAssigneeId() != null) {
            User assignee = new User();
            assignee.setId(taskDTO.getAssigneeId());
            task.setAssignee(assignee);
        }

        //Comment IDs -> Comments
        if (taskDTO.getCommentIds() != null) {
            List<Comment> comments = taskDTO.getCommentIds().stream()
                    .map(commentId->{
                        Comment comment = new Comment();
                        comment.setId(commentId);
                        return comment;
                    })
                    .collect(Collectors.toList());
            task.setComments(comments);
        }

        return task;
    }
}
