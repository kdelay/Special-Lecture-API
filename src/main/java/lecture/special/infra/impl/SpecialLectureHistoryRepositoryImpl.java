package lecture.special.infra.impl;

import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.domain.repository.SpecialLectureHistoryRepository;
import lecture.special.infra.entity.user.User;
import lecture.special.infra.jpa.JpaSpecialLectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpecialLectureHistoryRepositoryImpl implements SpecialLectureHistoryRepository {

    private final JpaSpecialLectureHistoryRepository jpaHistoryRepository;

    @Override
    public void save(SpecialLectureHistory specialLectureHistory) {
        jpaHistoryRepository.save(specialLectureHistory);
    }

    @Override
    public Optional<SpecialLectureHistory> findByUser(User user) {
        return jpaHistoryRepository.findByUser(user);
    }

    @Override
    public Optional<SpecialLectureHistory> findByUserAndSchedule(User user, Schedule schedule) {
        return jpaHistoryRepository.findByUserAndSchedule(user, schedule);
    }
}
