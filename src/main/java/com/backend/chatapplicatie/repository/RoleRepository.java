package com.backend.chatapplicatie.repository;

import java.util.Optional;

import com.backend.chatapplicatie.models.ERole;
import com.backend.chatapplicatie.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
