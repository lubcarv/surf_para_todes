package dev.surfparatodes.surfparatodes.repository;

import dev.surfparatodes.surfparatodes.model.user.userlogin.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
    UserLogin findByEmail(String email);
}
