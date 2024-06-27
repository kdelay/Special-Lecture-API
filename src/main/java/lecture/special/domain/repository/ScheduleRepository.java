package lecture.special.domain.repository;

import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository {

    Optional<Schedule> findById(Long id);

    // 특정 특강과 특정 날짜의 일정 조회
    Optional<Schedule> findBySpecialLectureAndSpeLecDate(SpecialLecture specialLecture, LocalDate speLecDate);
}
