package lecture.special.infrastructure.repository;

import lecture.special.infrastructure.db.ScheduleEntity;
import lecture.special.infrastructure.db.SpecialLectureHistoryEntity;
import lecture.special.infrastructure.db.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaSpecialLectureHistoryRepository extends JpaRepository<SpecialLectureHistoryEntity, Long> {
    Optional<SpecialLectureHistoryEntity> findByUser(UserEntity userEntity);

    Optional<SpecialLectureHistoryEntity> findByUserAndSchedule(UserEntity userEntity, ScheduleEntity scheduleEntity);
}