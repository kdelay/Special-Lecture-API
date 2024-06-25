package lecture.special.infra.repository.lecture.history;

import lecture.special.domain.model.lecture.history.SpecialLectureHistory;
import lecture.special.domain.model.lecture.history.SpecialLectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpecialLectureHistoryImpl implements SpecialLectureHistoryRepository {

    private final JpaSpecialLectureHistoryRepository jpaHistoryRepository;

    @Override
    public void save(SpecialLectureHistory specialLectureHistory) {
        jpaHistoryRepository.save(specialLectureHistory);
    }
}
