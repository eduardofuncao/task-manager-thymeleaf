package br.com.fiap.task_manager_security.config;

import br.com.fiap.task_manager_security.entity.User;
import br.com.fiap.task_manager_security.entity.UserRole;
import br.com.fiap.task_manager_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@fiap.com");
            admin.setCpf("12345678901");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(UserRole.ROLE_ADMIN);
            userRepository.save(admin);

            User manager = new User();
            manager.setName("Manager");
            manager.setEmail("manager@fiap.com");
            manager.setCpf("23456789012");
            manager.setPassword(passwordEncoder.encode("mngr"));
            manager.setRole(UserRole.ROLE_MANAGER);
            userRepository.save(manager);

            User collaborator = new User();
            collaborator.setName("Collaborator User");
            collaborator.setEmail("collaborator@fiap.com");
            collaborator.setCpf("34567890123");
            collaborator.setPassword(passwordEncoder.encode("cola"));
            collaborator.setRole(UserRole.ROLE_COLLABORATOR);
            userRepository.save(collaborator);

            System.out.println("Base users created successfully");
        }
    }
}
