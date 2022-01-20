package com.springboot.blog.security;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // since we can log in using username or email we change the variable name to usernameOrEmail so we can provide
    // either username or email to log in
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        // retrieve user from database
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("User not found with: " + usernameOrEmail);
                });

        // convert user into UserDetails (spring provided user)
        // we can create our own UserDetails class, or we can use Spring provided class
        // we can call constructor on Spring provided User class which takes in email, password, and granted authority,
        // but we have set of roles which we need to convert to granted authority
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {

        // convert our roles to granted authorities
        // If you are using Spring's default security configuration, a "ROLE_" prefix is needed.
        // Usually, the basic values are: ROLE_USER and ROLE_ADMIN.

        return roles.stream()
                .map((Role role) -> {
                    return new SimpleGrantedAuthority("ROLE_"+role.getName());
                }).collect(Collectors.toList());
    }
}
