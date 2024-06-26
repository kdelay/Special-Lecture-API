package lecture.special.domain.model.lecture;

import java.util.List;

public interface SpecialLectureRepository {

    SpecialLecture findBySpeLecName(String speLecName);

    List<SpecialLecture> findAll();
}
