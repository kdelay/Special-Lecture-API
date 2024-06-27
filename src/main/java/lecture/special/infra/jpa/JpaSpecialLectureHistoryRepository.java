package lecture.special.infra.jpa;

import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.infra.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaSpecialLectureHistoryRepository extends JpaRepository<SpecialLectureHistory, Long> {

    Optional<SpecialLectureHistory> findByUser(User user);
}