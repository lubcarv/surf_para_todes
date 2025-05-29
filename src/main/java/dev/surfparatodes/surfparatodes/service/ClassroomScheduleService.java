package dev.surfparatodes.surfparatodes.service;

import dev.surfparatodes.surfparatodes.model.user.classroomschedule.ClassroomSchedule;
import dev.surfparatodes.surfparatodes.model.user.classroomschedule.ClassroomScheduleId;
import dev.surfparatodes.surfparatodes.repository.ClassroomScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomScheduleService {
    private final ClassroomScheduleRepository classroomScheduleRepository;

    public ClassroomSchedule findById(Integer classroomId, Integer scheduleId) {
        ClassroomScheduleId id = new ClassroomScheduleId(classroomId, scheduleId);
        return classroomScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Associação de aula e horário não encontrada para Class ID: " +
                                classroomId + " e Schedule ID: " + scheduleId));
    }


    public List<ClassroomSchedule> findByClassroomId(Integer classroomId) {
        return classroomScheduleRepository.findByClassroomId(classroomId);
    }

    public List<ClassroomSchedule> findByScheduleId(Integer scheduleId) {
        return classroomScheduleRepository.findByScheduleId(scheduleId);
    }

    public List<ClassroomSchedule> findAll() {
        return classroomScheduleRepository.findAll();
    }
}
