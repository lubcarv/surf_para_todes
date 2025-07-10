package dev.surfparatodes.surfparatodes.service;

import dev.surfparatodes.surfparatodes.enums.UserRole;
import dev.surfparatodes.surfparatodes.model.user.user.Users;
import dev.surfparatodes.surfparatodes.repository.ClassroomTeacherChecker;
import dev.surfparatodes.surfparatodes.repository.UserLoginRepository;
import dev.surfparatodes.surfparatodes.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final ClassroomTeacherChecker classroomTeacherChecker;



    public  Users createUser(Users user) {
        user.setActive(true);
        return userRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado para exclusão.");
        }
        if (classroomTeacherChecker.existsByTeacherId((long) id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Usuário não pode ser excluído, pois está vinculado a uma ou mais turmas.");
        }
        userLoginRepository.deleteByUserId((long) id);
        userRepository.deleteById(id);
    }

    public List<Users> getUsersByUserRole(UserRole userRole) {
        return userRepository.findByUserRole(userRole);
    }

    public Users updateUser(int id, Users userDetails) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        updateUserData(user, userDetails);
        return userRepository.save(user);
    }

    private void updateUserData(Users user, Users userDetails) {
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

    public List<Integer> validateTeacherIds(Set<Integer> teacherIds) {
        List<Users> users = userRepository.findAllById(teacherIds);

        return teacherIds.stream()
                .filter(id-> users.stream()
                        .noneMatch(user-> user.getId().equals(id) && user.getUserRole() == UserRole.PROFESSOR))
                        .toList();
    }
}