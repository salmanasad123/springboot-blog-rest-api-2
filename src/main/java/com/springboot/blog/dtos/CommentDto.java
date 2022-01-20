package com.springboot.blog.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentDto {

    private Long id;

    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "Message Body should not be null or empty")
    @Size(min = 10, message = "Message Body should contain at least 10 characters")
    private String messageBody;
}
