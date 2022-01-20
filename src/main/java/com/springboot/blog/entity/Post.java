package com.springboot.blog.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// we are creating tables through hibernate, hibernate will create a table for this entity in the database

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor  // we need a no arg constructor because hibernate uses it
@Entity
// if we want to make any column value as unique we need to add unique constraint, title column values will be unique
@Table(name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // strategy for generating primary key
    private Long id;

    @Column(name = "title", nullable = false)   // field is not null (it should have a value)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    // when post is saved all comments are saved and when post is deleted, comments should be deleted so we used
    // Cascade type as ALL, orphanRemoval is set to true so when we remove post comment should be removed as well
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

}
