package dev.surfparatodes.surfparatodes.repository;

import dev.surfparatodes.surfparatodes.enums.UserRole;
import dev.surfparatodes.surfparatodes.model.user.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUserRole(UserRole userRole);

    List<User> findAll();
}
