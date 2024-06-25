package lecture.special.infra.repository.lecture;

import lecture.special.domain.model.lecture.SpecialLecture;
import lecture.special.domain.model.lecture.SpecialLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpecialLectureImpl implements SpecialLectureRepository {

    private final JpaSpecialLectureRepository jpaLectureRepository;

    @Override
    public SpecialLecture findBySpeLecName(String speLecName) {
        return jpaLectureRepository.findBySpeLecName(speLecName).orElse(null);
    }
}
