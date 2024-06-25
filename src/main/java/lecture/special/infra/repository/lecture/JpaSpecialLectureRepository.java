package lecture.special.infra.repository.lecture;

import lecture.special.domain.model.lecture.SpecialLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSpecialLectureRepository extends JpaRepository<SpecialLecture, Long> {
}
