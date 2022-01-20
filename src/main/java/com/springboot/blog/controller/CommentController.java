package com.springboot.blog.controller;

import com.springboot.blog.dtos.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto,
                                                    @PathVariable(name = "postId") Long postId) {

        CommentDto newPost = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping("posts/{postId}/comments")
    public List<CommentDto> getAllCommentsForPost(@PathVariable(name = "postId") Long postId) {

        return commentService.getAllCommentsForPost(postId);
    }

    @GetMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentWithId(@PathVariable(name = "postId") Long postId,
                                                       @PathVariable(name = "commentId") Long commentId) {

        CommentDto commentDto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentWithId(@PathVariable(name = "postId") Long postId,
                                                          @PathVariable(name = "commentId") Long commentId,
                                                          @Valid @RequestBody CommentDto commentDto) {
        CommentDto updatedCommentDto = commentService.updateCommentById(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedCommentDto, HttpStatus.OK);
    }

    @DeleteMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentWitId(@PathVariable(name = "postId") Long postId,
                                                     @PathVariable(name = "commentId") Long commentId) {
        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("Comment with id " + commentId + " has been deleted", HttpStatus.OK);
    }

}
