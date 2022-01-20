package com.springboot.blog.controller;


import com.springboot.blog.dtos.PostDto;
import com.springboot.blog.dtos.PostDtoForPagination;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/posts")
public class PostController {

    private PostService postService;

    // we are injecting interface not concrete implementation, so we are doing loose coupling
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // create blog post rest api
    // @Valid will enable the hibernat validator, so the request body will be validated as per rules defined on PostDto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")   // only admin can access this route
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {

        PostDto newPost = postService.createPost(postDto);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    // get all posts rest api
    // page number and page size are optional, so we are passing them as query parameters and set the required to false
    // and since the parameters are optional we are providing default values
    @GetMapping
    public PostDtoForPagination getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    // get a post with id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id) {

        PostDto postDto = postService.getPostById(id);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    // update post by id rest api
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // providing methid level security so only admin can update posts
    public ResponseEntity<PostDto> updatePostById(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") Long id) {

        PostDto updatedPost = postService.updatePostById(postDto, id);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // delete post rest api
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePostById(@PathVariable(name = "id") Long id) {

        postService.deletePostById(id);
        return new ResponseEntity<String>("Post with id: " + id + " has been deleted", HttpStatus.OK);
    }
}
