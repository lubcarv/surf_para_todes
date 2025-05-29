package dev.surfparatodes.surfparatodes.repository;

import dev.surfparatodes.surfparatodes.model.user.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByShift(String shift);
    List<Schedule> findByScheduleTime(String scheduleTime);
    List<Schedule> findByActiveTrue();
    List<Schedule> findByActiveFalse();
}
