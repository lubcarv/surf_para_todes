package dev.surfparatodes.surfparatodes.controller;

import dev.surfparatodes.surfparatodes.model.user.classroom.ClassroomCreateDTO;
import dev.surfparatodes.surfparatodes.model.user.classroom.ClassroomResponseDTO;
import dev.surfparatodes.surfparatodes.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class Classroom {

    private final ClassroomService classroomService;

    @PostMapping
    public ResponseEntity<ClassroomResponseDTO> create(@RequestBody @Valid ClassroomCreateDTO dto) {
        ClassroomResponseDTO responseDTO = classroomService.createClassroom(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ClassroomResponseDTO>> getAll() {
        List<ClassroomResponseDTO> classrooms = classroomService.findAll();
        return ResponseEntity.ok(classrooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> getById(@PathVariable Integer id) {
        return classroomService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
