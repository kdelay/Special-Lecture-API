package lecture.special.infra.jpa;

import lecture.special.infra.entity.lecture.SpecialLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaSpecialLectureRepository extends JpaRepository<SpecialLecture, Long> {

    Optional<SpecialLecture> findBySpeLecName(String speLecName);
}
