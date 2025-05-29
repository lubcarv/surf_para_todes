package dev.surfparatodes.surfparatodes.controller;

import dev.surfparatodes.surfparatodes.converters.user.ScheduleMapper;
import dev.surfparatodes.surfparatodes.model.user.schedule.ScheduleCreateDTO;
import dev.surfparatodes.surfparatodes.model.user.schedule.ScheduleResponseDTO;
import dev.surfparatodes.surfparatodes.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class Schedule {
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> getById(@PathVariable Integer id) {
        dev.surfparatodes.surfparatodes.model.user.schedule.Schedule schedule = scheduleService.findById(id);
        return ResponseEntity.ok(scheduleMapper.toResponseDTO(schedule));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> getAll(
            @RequestParam(required = false) String shift,
            @RequestParam(required = false) String time,
            @RequestParam(defaultValue = "true") boolean active) {

        List<dev.surfparatodes.surfparatodes.model.user.schedule.Schedule> schedules;
        if (shift != null || time != null) {
            schedules = scheduleService.findByFilters(shift, time);
        } else {
            schedules = scheduleService.findByActive(active);
        }

        List<ScheduleResponseDTO> dtos = schedules.stream()
                .map(scheduleMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> create(@RequestBody @Valid ScheduleCreateDTO dto) {
        dev.surfparatodes.surfparatodes.model.user.schedule.Schedule schedule = scheduleMapper.toEntity(dto);
        dev.surfparatodes.surfparatodes.model.user.schedule.Schedule createdSchedule = scheduleService.create(schedule);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleMapper.toResponseDTO(createdSchedule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid ScheduleCreateDTO dto) {
        dev.surfparatodes.surfparatodes.model.user.schedule.Schedule schedule = scheduleMapper.toEntity(dto);
        schedule.setId(id);
        dev.surfparatodes.surfparatodes.model.user.schedule.Schedule updatedSchedule = scheduleService.update(schedule);
        return ResponseEntity.ok(scheduleMapper.toResponseDTO(updatedSchedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        scheduleService.softDelete(id);
        return ResponseEntity.ok("Agenda desativada com sucesso.");
    }
}

