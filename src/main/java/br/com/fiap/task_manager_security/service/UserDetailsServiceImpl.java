package br.com.fiap.task_manager_security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl {

    private final InMemoryUserDetailsManager userDetailsManager;

    public UserDetailsServiceImpl(UserDetailsService userDetailsService) {
        if (!(userDetailsService instanceof InMemoryUserDetailsManager)) {
            throw new IllegalArgumentException("UserDetailsService must be an instance of InMemoryUserDetailsManager");
        }
        this.userDetailsManager = (InMemoryUserDetailsManager) userDetailsService;
    }

    public List<UserDetails> getAllUsers() {
        List<UserDetails> users = new ArrayList<>();
        userDetailsManager.loadUserByUsername("admin");
        // Since InMemoryUserDetailsManager doesn't provide a method to get all users,
        // we would need to keep track of users separately in a real implementation
        // This is a limitation of using InMemoryUserDetailsManager
        return users;
    }

    public void createUser(String username, String password, String role, String email, String cpf) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

        UserDetails user = User.builder()
                .username(username)
                .password(password)  // Assuming password is already encoded
                .authorities(authorities)
                .build();

        userDetailsManager.createUser(user);
    }

    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }

    public void deleteUser(String username) {
        userDetailsManager.deleteUser(username);
    }

    public UserDetails loadUserByUsername(String username) {
        return userDetailsManager.loadUserByUsername(username);
    }
}
