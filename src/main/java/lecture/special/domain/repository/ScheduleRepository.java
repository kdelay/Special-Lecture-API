package lecture.special.domain.repository;

import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;

public interface ScheduleRepository {

    Schedule findById(Long id);

    Schedule findBySpecialLecture(SpecialLecture specialLecture);
}
