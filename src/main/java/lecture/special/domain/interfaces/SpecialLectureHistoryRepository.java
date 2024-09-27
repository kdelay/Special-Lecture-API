package lecture.special.domain.interfaces;

import lecture.special.domain.Schedule;
import lecture.special.domain.SpecialLectureHistory;
import lecture.special.domain.User;

public interface SpecialLectureHistoryRepository {

    void save(SpecialLectureHistory specialLectureHistory);

    SpecialLectureHistory findByUser(User user);

    SpecialLectureHistory findByUserAndSchedule(User user, Schedule schedule);
}
