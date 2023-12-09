package com.backend.chatapplicatie;

import com.backend.chatapplicatie.models.ERole;
import com.backend.chatapplicatie.models.Role;
import com.backend.chatapplicatie.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InsertData implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public InsertData(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        // Check if roles are there
        if (roleRepository.findByName(ERole.USER_ROLE).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(ERole.USER_ROLE);
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName(ERole.ADMIN_ROLE).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(ERole.ADMIN_ROLE);
            roleRepository.save(adminRole);
        }
    }
}
