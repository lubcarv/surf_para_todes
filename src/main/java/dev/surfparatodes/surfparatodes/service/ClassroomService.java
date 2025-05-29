package dev.surfparatodes.surfparatodes.service;

import dev.surfparatodes.surfparatodes.converters.user.ClassroomMapper;
import dev.surfparatodes.surfparatodes.enums.UserRole;
import dev.surfparatodes.surfparatodes.model.user.classroom.Classroom;
import dev.surfparatodes.surfparatodes.model.user.classroom.ClassroomCreateDTO;
import dev.surfparatodes.surfparatodes.model.user.classroom.ClassroomResponseDTO;
import dev.surfparatodes.surfparatodes.model.user.user.User;
import dev.surfparatodes.surfparatodes.repository.ClassroomRepository;
import dev.surfparatodes.surfparatodes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final ClassroomMapper classroomMapper;

    @Autowired
    public ClassroomService(
            ClassroomRepository classroomRepository,
            UserRepository userRepository,
            ClassroomMapper classroomMapper
    ) {
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;
        this.classroomMapper = classroomMapper;
    }

    @Transactional
    public ClassroomResponseDTO createClassroom(ClassroomCreateDTO createDTO) {
        Set<User> teachers = new HashSet<>(userRepository.findAllById(createDTO.getTeacherIds()));

        List<User> invalidUsers = teachers.stream()
                .filter(user -> user.getUserRole() != UserRole.PROFESSOR)
                .toList();

        if (!invalidUsers.isEmpty()) {
            String invalidNames = invalidUsers.stream()
                    .map(User::getDisplayName)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Os seguintes usuários não são professores: " + invalidNames);
        }

        Classroom classroom = classroomMapper.toEntity(createDTO, teachers);
        Classroom saved = classroomRepository.save(classroom);
        return classroomMapper.toResponseDTO(saved);
    }

    public List<ClassroomResponseDTO> findAll() {
        return classroomRepository.findAll()
                .stream()
                .map(classroomMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ClassroomResponseDTO> findById(Integer id) {
        return classroomRepository.findById(id)
                .map(classroomMapper::toResponseDTO);
    }

    // ✅ NOVO MÉTODO ADICIONADO
    public Classroom findEntityById(Integer id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Classroom not found with id: " + id));
    }
}
