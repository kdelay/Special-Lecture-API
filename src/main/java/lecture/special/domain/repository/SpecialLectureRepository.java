package lecture.special.domain.repository;

import lecture.special.infra.entity.lecture.SpecialLecture;

import java.util.List;
import java.util.Optional;

public interface SpecialLectureRepository {

    Optional<SpecialLecture> findBySpeLecName(String speLecName);

    List<SpecialLecture> findAll();
}
