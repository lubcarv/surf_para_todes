package dev.surfparatodes.surfparatodes.service;

import dev.surfparatodes.surfparatodes.enums.UserRole;
import dev.surfparatodes.surfparatodes.model.user.user.User;
import dev.surfparatodes.surfparatodes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public  User createUser(User user) {
        user.setActive(true);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado para exclusão");
        }
        userRepository.deleteById(id);
    }

    public List<User> getUsersByUserRole(UserRole userRole) {
        return userRepository.findByUserRole(userRole);
    }

    public User updateUser(int id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        updateUserData(user, userDetails);
        return userRepository.save(user);
    }

    private void updateUserData(User user, User userDetails) {
        user.setRegisterName(userDetails.getRegisterName());
        user.setSocialName(userDetails.getSocialName());
        user.setBirthDate(userDetails.getBirthDate());
        user.setGuardianName(userDetails.getGuardianName());
        user.setGuardianRelationship(userDetails.getGuardianRelationship());
        user.setGuardianPhone(userDetails.getGuardianPhone());
        user.setGender(userDetails.getGender());
        user.setRace(userDetails.getRace());
        user.setPhone(userDetails.getPhone());
        user.setEmail(userDetails.getEmail());
        user.setUserRole(userDetails.getUserRole());

    }

}