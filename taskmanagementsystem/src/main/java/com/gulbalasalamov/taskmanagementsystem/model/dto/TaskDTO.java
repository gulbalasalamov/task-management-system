package com.gulbalasalamov.taskmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Long assigneeId;
    private List<Long> commentIds;
    private String priority;
}

