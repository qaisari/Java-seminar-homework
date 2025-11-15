package com.example.seminarHomework.common.initializer;

import com.example.seminarHomework.core.repository.UserRepo;
import com.example.seminarHomework.core.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.user1.password}")
    private String user1Password;

    @Bean
    CommandLineRunner initDatabase(UserRepo repository) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return args -> {
            if(repository.count() == 0) {
                repository.save(new User("Admin","admin@gmail.com",passwordEncoder.encode(adminPassword),"ROLE_ADMIN"));
                repository.save(new User("User1","user@gmail.com",passwordEncoder.encode(user1Password),"ROLE_USER"));
            }
        };
    }
}
