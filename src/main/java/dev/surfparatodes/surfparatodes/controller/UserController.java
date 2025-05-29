package dev.surfparatodes.surfparatodes.controller;

import dev.surfparatodes.surfparatodes.enums.UserRole;
import dev.surfparatodes.surfparatodes.converters.user.UserMapper;
import dev.surfparatodes.surfparatodes.model.user.user.User;
import dev.surfparatodes.surfparatodes.model.user.user.UserCreateDTO;
import dev.surfparatodes.surfparatodes.model.user.user.UserResponseDTO;
import dev.surfparatodes.surfparatodes.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // Criar usuário
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserCreateDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(savedUser));
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Listar por tipo
    @GetMapping("/type/{userRole}")
    public ResponseEntity<List<User>> getUsersByType(@PathVariable UserRole userRole) {
        return ResponseEntity.ok(userService.getUsersByUserRole(userRole));
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
