package lecture.special.domain.repository;

import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.infra.entity.user.User;

import java.util.Optional;

public interface SpecialLectureHistoryRepository {

    void save(SpecialLectureHistory specialLectureHistory);

    Optional<SpecialLectureHistory> findByUser(User user);

    Optional<SpecialLectureHistory> findByUserAndSchedule(User user, Schedule schedule);
}
