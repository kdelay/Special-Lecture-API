package lecture.special.domain.interfaces;

import lecture.special.domain.SpecialLecture;

import java.util.List;

public interface SpecialLectureRepository {

    SpecialLecture findBySpecialLectureName(String specialLectureName);

    List<SpecialLecture> findAll();
}




