package br.com.fiap.task_manager_security.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/users").hasRole("ADMIN")
                        .requestMatchers("/reports").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/tasks/new", "/tasks/edit/**").hasRole("MANAGER")
                        .requestMatchers("/tasks/**", "/tasks").hasAnyRole("MANAGER", "COLLABORATOR")
                        .requestMatchers("/profile", "/home").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails manager1 = User.builder()
                .username("gerente")
                .password(passwordEncoder().encode("gerente123"))
                .roles("MANAGER")
                .build();

        UserDetails collaborator1 = User.builder()
                .username("colaborador1")
                .password(passwordEncoder().encode("colab123"))
                .roles("COLLABORATOR")
                .build();

        UserDetails collaborator2 = User.builder()
                .username("colaborador2")
                .password(passwordEncoder().encode("colab123"))
                .roles("COLLABORATOR")
                .build();

        manager.createUser(admin);
        manager.createUser(manager1);
        manager.createUser(collaborator1);
        manager.createUser(collaborator2);

        return manager;
    }
}
