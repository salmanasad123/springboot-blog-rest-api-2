package com.springboot.blog.dtos;

import lombok.Data;

@Data
public class SignupDto {

    private String name;
    private String username;
    private String email;
    private String password;
}
