package lecture.special.infra.repository.lecture;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpecialLectureImpl {

    private final JpaSpecialLectureRepository jpaSpecialLectureRepository;
}
