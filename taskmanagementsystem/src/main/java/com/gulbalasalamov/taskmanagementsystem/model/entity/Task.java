package com.gulbalasalamov.taskmanagementsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskPriority;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(sequenceName = "task_seq", name = "task_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String title;

    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;

    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "id")
    private User assignee;
}
