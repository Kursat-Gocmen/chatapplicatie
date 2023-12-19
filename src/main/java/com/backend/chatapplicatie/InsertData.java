package com.backend.chatapplicatie;

import com.backend.chatapplicatie.models.ERole;
import com.backend.chatapplicatie.models.Role;
import com.backend.chatapplicatie.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InsertData implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        // Check if roles are there
        if (roleRepository.findByName(ERole.USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(ERole.USER);
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName(ERole.ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(ERole.ADMIN);
            roleRepository.save(adminRole);
        }
    }
}
