package lecture.special.infra.repository.lecture.history;

import lecture.special.domain.model.lecture.history.SpecialLectureHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSpecialLectureHistoryRepository extends JpaRepository<SpecialLectureHistory, Long> {
}
