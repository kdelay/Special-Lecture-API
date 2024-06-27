package lecture.special.domain.repository;

import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;

import java.util.Optional;

public interface ScheduleRepository {

    Optional<Schedule> findById(Long id);

    Optional<Schedule> findBySpecialLecture(SpecialLecture specialLecture);
}
