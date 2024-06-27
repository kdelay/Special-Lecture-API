package lecture.special.infra.jpa;

import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long> {

    // 특정 특강과 특정 날짜의 일정 조회
    Optional<Schedule> findBySpecialLectureAndSpeLecDate(SpecialLecture specialLecture, LocalDate speLecDate);
}
