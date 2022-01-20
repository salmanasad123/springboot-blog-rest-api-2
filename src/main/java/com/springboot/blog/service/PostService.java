package com.springboot.blog.service;

import com.springboot.blog.dtos.PostDto;
import com.springboot.blog.dtos.PostDtoForPagination;

public interface PostService {

    PostDto createPost(PostDto postDto);

    PostDtoForPagination getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(Long id);

    PostDto updatePostById(PostDto postDto, Long id);

    void deletePostById(Long id);
}
