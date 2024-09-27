package lecture.special.infrastructure.repository;

import lecture.special.infrastructure.db.ScheduleEntity;
import lecture.special.infrastructure.db.SpecialLectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface JpaScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    Optional<ScheduleEntity> findBySpecialLectureEntityAndSpecialLectureDate(SpecialLectureEntity specialLectureEntity, LocalDate specialLectureDate);
}