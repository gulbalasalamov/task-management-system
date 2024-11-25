package com.gulbalasalamov.taskmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Long userId;
    private Long taskId;
}


