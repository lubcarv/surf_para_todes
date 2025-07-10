package dev.surfparatodes.surfparatodes.repository;

import dev.surfparatodes.surfparatodes.enums.UserRole;
import dev.surfparatodes.surfparatodes.model.user.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Integer> {
    List<Users> findByUserRole(UserRole userRole);

    List<Users> findAll();
}
