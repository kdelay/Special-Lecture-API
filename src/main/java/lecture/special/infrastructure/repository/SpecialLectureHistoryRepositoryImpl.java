package lecture.special.infrastructure.repository;

import lecture.special.domain.Schedule;
import lecture.special.domain.SpecialLectureHistory;
import lecture.special.domain.User;
import lecture.special.domain.interfaces.SpecialLectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class SpecialLectureHistoryRepositoryImpl implements SpecialLectureHistoryRepository {

    private final JpaSpecialLectureHistoryRepository jpaHistoryRepository;

    @Override
    public void save(SpecialLectureHistory specialLectureHistory) {
        jpaHistoryRepository.save(SpecialLectureHistory.toEntity(specialLectureHistory));
    }

    @Override
    public SpecialLectureHistory findByUser(User user) {
        return SpecialLectureHistory.toDomain(
                jpaHistoryRepository.findByUser(User.toEntity(user))
                        .orElseThrow(() -> new NoSuchElementException("히스토리에 유저가 존재하지 않습니다."))
        );
    }

    @Override
    public SpecialLectureHistory findByUserAndSchedule(User user, Schedule schedule) {
        return SpecialLectureHistory.toDomain(
                jpaHistoryRepository.findByUserAndSchedule(User.toEntity(user), Schedule.toEntity(schedule))
                        .orElseThrow(() -> new IllegalStateException(user.getUserId() + "님은 " +
                                schedule.getSpecialLecture().getSpecialLectureName() + " 특강 신청에 실패하였습니다."))
        );
    }
}
