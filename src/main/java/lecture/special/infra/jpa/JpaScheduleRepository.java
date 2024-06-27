package lecture.special.infra.jpa;

import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findBySpecialLecture(SpecialLecture specialLecture);
}
