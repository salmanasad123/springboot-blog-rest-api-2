package com.springboot.blog.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// class for post pagination response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDtoForPagination {

    private List<PostDto> content;
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean last;


}
