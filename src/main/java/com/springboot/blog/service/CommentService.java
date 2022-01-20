package com.springboot.blog.service;

import com.springboot.blog.dtos.CommentDto;

import java.util.List;

public interface CommentService {

    public CommentDto createComment(Long postId, CommentDto commentDto);

    public List<CommentDto> getAllCommentsForPost(Long postId);

    public CommentDto getCommentById(Long postId, Long commentId);

    public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentDto);

    public void deleteCommentById(Long postId, Long commentId);
}
