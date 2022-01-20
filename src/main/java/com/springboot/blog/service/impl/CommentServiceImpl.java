package com.springboot.blog.service.impl;

import com.springboot.blog.dtos.CommentDto;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.excpetion.BlogApiException;
import com.springboot.blog.excpetion.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        // convert DTO to entity because our repository save method accept entity as argument
        Comment comment = mapToEntity(commentDto);

        // get the post entity by Id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Post", "id", postId);
                });

        // set post to comment entity
        comment.setPost(post);

        // save the comment to database
        Comment newComment = commentRepository.save(comment);

        CommentDto newCommentDto = mapToDto(newComment);

        return newCommentDto;
    }

    @Override
    public List<CommentDto> getAllCommentsForPost(Long postId) {

        // find the post with the given id
        postRepository.findById(postId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Post", "id", postId);
                });

        // find all comments for a post using our custom query method
        List<Comment> comments = commentRepository.findByPostId(postId);

        // map all comments we get to commentDto
        List<CommentDto> commentDtoList = comments.stream()
                .map((Comment comment) -> {
                    return mapToDto(comment);
                })
                .collect(Collectors.toList());

        return commentDtoList;
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        // find the post with the given id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Post", "id", postId);
                });

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Comment", "id", commentId);
                });

        // check whether the comment belongs to the particular post (with post id passed above),
        // if not then throw the exception

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post with Id " + postId);
        }

        CommentDto commentDto = mapToDto(comment);

        return commentDto;
    }

    @Override
    public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentDto) {
        // find the post with the given id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Post", "id", postId);
                });

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Comment", "id", commentId);
                });

        // check whether the comment belongs to the particular post (with post id passed above),
        // if not then throw the exception
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post with id " + postId);
        }

        // update the fields for the found comment
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setMessageBody(commentDto.getMessageBody());

        // save the comment
        Comment updatedComment = commentRepository.save(comment);

         // convert the updated comment to commentDto and return

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {

        // find the post with the given id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Post", "id", postId);
                });

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Comment", "id", commentId);
                });

        // check whether the comment belongs to the particular post (with post id passed above),
        // if not then throw the exception
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post with id " + postId);
        }

        commentRepository.delete(comment);
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setEmail(commentDto.getEmail());
        comment.setName(commentDto.getName());
        comment.setMessageBody(commentDto.getMessageBody());

        return comment;
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setEmail(comment.getEmail());
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setMessageBody(comment.getMessageBody());

        return commentDto;
    }
}
