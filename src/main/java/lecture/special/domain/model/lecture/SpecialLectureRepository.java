package lecture.special.domain.model.lecture;

public interface SpecialLectureRepository {

    SpecialLecture findBySpeLecName(String speLecName);
}
