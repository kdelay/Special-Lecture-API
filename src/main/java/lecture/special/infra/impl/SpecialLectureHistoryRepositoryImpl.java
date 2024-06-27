package lecture.special.infra.impl;

import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.domain.repository.SpecialLectureHistoryRepository;
import lecture.special.infra.entity.user.User;
import lecture.special.infra.jpa.JpaSpecialLectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpecialLectureHistoryRepositoryImpl implements SpecialLectureHistoryRepository {

    private final JpaSpecialLectureHistoryRepository jpaHistoryRepository;

    @Override
    public void save(SpecialLectureHistory specialLectureHistory) {
        jpaHistoryRepository.save(specialLectureHistory);
    }

    @Override
    public SpecialLectureHistory findByUser(User user) {
        return jpaHistoryRepository.findByUser(user).orElse(null);
    }
}
