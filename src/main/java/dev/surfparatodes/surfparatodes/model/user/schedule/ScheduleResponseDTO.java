package dev.surfparatodes.surfparatodes.model.user.schedule;
import dev.surfparatodes.surfparatodes.model.user.classroomschedule.ClassroomScheduleResponseDTO;

import java.util.Set;

public record ScheduleResponseDTO(
        Integer id,
        String shift,
        String schedule_time,
        Boolean active,
        Set<ClassroomScheduleResponseDTO> classes

) {
}
