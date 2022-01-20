package com.springboot.blog.service.impl;

import com.springboot.blog.dtos.PostDto;
import com.springboot.blog.dtos.PostDtoForPagination;
import com.springboot.blog.entity.Post;
import com.springboot.blog.excpetion.ResourceNotFoundException;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// make this class available for auto-detection while component scanning, marking this class as spring managed component
@Service
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    ModelMapper modelMapper;

    // if a class is spring bean (spring managed component) and its constructor only has one parameter
    // then we can omit @Autowired, but we are using it for clarity
    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // convert DTO to entity because our repository save method accept entity as argument
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setId(postDto.getId());
        post.setDescription(postDto.getDescription());

        // save post to database ... save method will return the newly created post
        Post newPost = postRepository.save(post);

        // return Post Dto to controller so convert Post Entity to Post Dto
        PostDto postResponse = new PostDto();
        postResponse.setId(newPost.getId());
        postResponse.setContent(newPost.getContent());
        postResponse.setTitle(newPost.getTitle());
        postResponse.setDescription(newPost.getDescription());

        // return post dto back to controller
        return postResponse;

    }

    @Override
    public PostDtoForPagination getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        // create sort instance to support sorting
        // if sort direction received from query param is asc then we set ascending order on sort object
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create pageable instance to support pagination
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // get content from page object i.e. posts above... get the contents for this page
        List<Post> listOfPosts = posts.getContent();

        // we want list of post Dto so we map it to dto
        List<PostDto> postDtos = listOfPosts.stream()
                .map((Post post) -> {
                    PostDto postDto = mapToDto(post);
                    return postDto;
                })
                .collect(Collectors.toList());

        PostDtoForPagination dtoForPagination = new PostDtoForPagination();
        dtoForPagination.setContent(postDtos);
        dtoForPagination.setPageNumber(posts.getNumber());
        dtoForPagination.setPageSize(posts.getSize());
        dtoForPagination.setTotalPages(posts.getTotalPages());
        dtoForPagination.setTotalElements(posts.getTotalElements());
        dtoForPagination.setLast(posts.isLast());

        return dtoForPagination;
    }

    @Override
    public PostDto getPostById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Post", "id", id);
                });

        return mapToDto(post);
    }

    @Override
    public PostDto updatePostById(PostDto postDto, Long id) {

        // get post by id which you want to update from the database
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Post", "id", id);
                });

        // update the post
        post.setDescription(post.getDescription());
        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());

        // save the post to the database (save method will create the post if it is not present in the database,
        // and it will update the post if it is found in the database
        Post updatedPost = postRepository.save(post);

        // convert the post to Dto before sending back to controller
        return mapToDto(updatedPost);

    }

    @Override
    public void deletePostById(Long id) {
        // get the post by id, if the post is not found then throw our custom exception
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Post", "id", id);
                });

        // we can also use deleteById method
        postRepository.delete(post);
    }


    // helper method to convert entitiy to DTO
    private PostDto mapToDto(Post post) {

        // we will use model mapper instead of manually mapping to DTO
        PostDto postDto = modelMapper.map(post, PostDto.class);

//        PostDto postDto = new PostDto();
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
//        postDto.setId(post.getId());

        return postDto;
    }

    private Post mapToEntity(PostDto postDto) {

        Post post = modelMapper.map(postDto, Post.class);

//        Post post = new Post();
//        post.setId(postDto.getId());
//        post.setContent(postDto.getContent());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());

        return post;
    }
}
