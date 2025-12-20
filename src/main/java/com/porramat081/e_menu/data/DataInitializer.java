package com.porramat081.e_menu.data;

import com.porramat081.e_menu.model.Role;
import com.porramat081.e_menu.model.User;
import com.porramat081.e_menu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN" , "ROLE_USER");
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultUserIfNotExists();
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for (int i = 1; i <= 3; i++) {
            String defaultEmail = "user" + i + "@email.com";
            if (this.userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The user");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setRoles(Set.of(userRole));
            user.setPassword(passwordEncoder.encode("123456"));
            this.userRepository.save(user);
            System.out.println("Default user " + i + " created successfully.");
        }
    }

    private void createDefaultAdminIfNotExists(){
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@email.com";
            if (this.userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            this.userRepository.save(user);
            System.out.println("Default Admin " + i + " created successfully.");
        }
    }

    private void createDefaultRoleIfNotExists(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }
}
