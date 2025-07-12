package dev.surfparatodes.surfparatodes.controller;

import dev.surfparatodes.surfparatodes.enums.UserRole;
import dev.surfparatodes.surfparatodes.converters.user.UserMapper;
import dev.surfparatodes.surfparatodes.model.user.user.Users;
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
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // Criar usuário
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserCreateDTO userDTO) {
        Users user = userMapper.toEntity(userDTO);
        Users savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(savedUser));
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    // Listar por tipo
    @GetMapping("/type/{userRole}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByType(@PathVariable UserRole userRole) {
        List<UserResponseDTO> users = userService.getUsersByUserRole(userRole)
                .stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/alunos/inativos")
    public ResponseEntity<List<UserResponseDTO>> getInactiveStudents() {
        List<UserResponseDTO> alunosInativos = userService.getUsersByRoleAndStatus(UserRole.ALUNO, false)
                .stream()
                .map(userMapper::toDTO)
                .toList();

        return ResponseEntity.ok(alunosInativos);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable int id, @RequestBody Users user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
