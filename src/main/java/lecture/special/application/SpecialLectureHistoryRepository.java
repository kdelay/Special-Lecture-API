package lecture.special.application;

import lecture.special.domain.SpecialLectureHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialLectureHistoryRepository extends JpaRepository<SpecialLectureHistory, Long> {
}
