package com.gulbalasalamov.taskmanagementsystem.controller;

import com.gulbalasalamov.taskmanagementsystem.model.dto.CommentDTO;
import com.gulbalasalamov.taskmanagementsystem.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CommentDTO> addComment(@RequestParam Long taskId,
                                                 @RequestParam Long userId,
                                                 @RequestBody CommentDTO commentDTO,
                                                 Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CommentDTO createdComment = commentService.addComment(taskId, userId, commentDTO, userDetails);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{commentId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId,
                                                    @RequestBody CommentDTO commentDTO,
                                                    Authentication authentication) {
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO, currentUser);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        commentService.deleteComment(commentId, currentUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTask(@PathVariable Long taskId) {
        List<CommentDTO> comments = commentService.getCommentsByTask(taskId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUser(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUser(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
