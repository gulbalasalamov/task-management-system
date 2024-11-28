package com.gulbalasalamov.taskmanagementsystem.repository.specification;

import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskPriority;
import com.gulbalasalamov.taskmanagementsystem.model.enums.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> withFilters(String title,
                                                  TaskStatus taskStatus,
                                                  TaskPriority taskPriority,
                                                  Long authorId,
                                                  Long assigneeId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (taskStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("taskStatus"), taskStatus));
            }
            if (taskPriority != null) {
                predicates.add(criteriaBuilder.equal(root.get("taskPriority"), taskPriority));
            }
            if (authorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("id"), authorId));
            }
            if (assigneeId != null) {
                predicates.add(criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
