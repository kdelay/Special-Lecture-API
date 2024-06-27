package lecture.special.domain.repository;

import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.infra.entity.user.User;

public interface SpecialLectureHistoryRepository {

    void save(SpecialLectureHistory specialLectureHistory);

    SpecialLectureHistory findByUser(User user);
}
