package lecture.special.application;

import lecture.special.domain.SpecialLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialLectureRepository extends JpaRepository<SpecialLecture, Long> {
}
