package lecture.special.domain.repository;

import lecture.special.infra.entity.lecture.SpecialLecture;

import java.util.List;

public interface SpecialLectureRepository {

    SpecialLecture findBySpeLecName(String speLecName);

    List<SpecialLecture> findAll();
}
