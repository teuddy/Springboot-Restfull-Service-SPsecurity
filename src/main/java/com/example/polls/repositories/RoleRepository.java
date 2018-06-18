package com.example.polls.repositories;

import com.example.polls.models.RoleName;
import com.example.polls.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles,Long> {

    Optional<Roles> findByName(RoleName roleName);
}
