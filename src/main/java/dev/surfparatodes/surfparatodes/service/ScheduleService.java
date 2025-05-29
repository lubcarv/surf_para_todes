package dev.surfparatodes.surfparatodes.service;

import dev.surfparatodes.surfparatodes.model.user.schedule.Schedule;
import dev.surfparatodes.surfparatodes.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Schedule findById(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule não encontrado com ID: " + id));
    }

    public Schedule create(Schedule schedule) {
        if (schedule.getActive() == null) {
            schedule.setActive(true);
        }
        return scheduleRepository.save(schedule);
    }

    public Schedule update(Schedule schedule) {
        Schedule existingSchedule = findById(schedule.getId());
        if (schedule.getActive() == null) {
            schedule.setActive(existingSchedule.getActive());
        }
        return scheduleRepository.save(schedule);
    }

    public void softDelete(Integer id) {
        Schedule schedule = findById(id);
        schedule.setActive(false);
        scheduleRepository.save(schedule);
    }

    public List<Schedule> findByFilters(String shift, String time) {
        // Implementar a lógica de filtros aqui
        if (shift != null && time != null) {
            // Criar consulta personalizada se necessário
            return scheduleRepository.findAll().stream()
                    .filter(s -> s.getShift().equals(shift) && s.getScheduleTime().equals(time))
                    .toList();
        } else if (shift != null) {
            return scheduleRepository.findByShift(shift);
        } else if (time != null) {
            return scheduleRepository.findByScheduleTime(time);
        }
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByActive(boolean active) {
        if (active) {
            return scheduleRepository.findByActiveTrue();
        }
        return scheduleRepository.findByActiveFalse();
    }
}
