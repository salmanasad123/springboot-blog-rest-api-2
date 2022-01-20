package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // jpa provides crud query methods, but sometimes we want to write our own e.g. retrieve user buy email id
    // jpa will create a query behind the scene for this method
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username); // check whether user exist by username or not

    Boolean existsByEmail(String email);
}
