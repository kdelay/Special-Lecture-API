package lecture.special.infra.repository.lecture.history;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpecialLectureHistoryImpl {

    private final JpaSpecialLectureHistoryRepository jpaSpecialLectureHistoryRepository;
}
