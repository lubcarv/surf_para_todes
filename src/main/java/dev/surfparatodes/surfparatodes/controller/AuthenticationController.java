package dev.surfparatodes.surfparatodes.controller;

import dev.surfparatodes.surfparatodes.infra.security.TokenService;
import dev.surfparatodes.surfparatodes.model.user.userlogin.*;
import dev.surfparatodes.surfparatodes.repository.UserLoginRepository;
import dev.surfparatodes.surfparatodes.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserLoginRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(data.email(), data.password());

        Authentication authentication = authenticationManager.authenticate(authToken);

        UserLogin user = (UserLogin) authentication.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (repository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().body("Usuário já existe.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());

        UserLogin newUser = new UserLogin(
                data.email(),
                data.fullName(),
                userRepository.getReferenceById(data.userId()),
                encryptedPassword,
                data.role(),
                data.phone()
        );

        repository.save(newUser);
        return ResponseEntity.ok("Usuário registado com sucesso");
    }
}