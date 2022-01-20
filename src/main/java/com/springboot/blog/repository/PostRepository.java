package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  no need to write code here because spring data repository extends JpaRepository interface
 *  which internally provides all CRUD methods and their implementation for us,
 *  Jpa repository also supports sorting and pagination
 *  Also no need to add @Repository annotation because JpaRepository has an implementation class called
 *  SimpleJpaRepository which provides @Repository and @Transactional annotation
 */

public interface PostRepository extends JpaRepository<Post, Long> {

}
