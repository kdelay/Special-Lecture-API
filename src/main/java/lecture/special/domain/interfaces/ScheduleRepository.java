package lecture.special.domain.interfaces;

import lecture.special.domain.Schedule;
import lecture.special.domain.SpecialLecture;

import java.time.LocalDate;

public interface ScheduleRepository {

    Schedule findById(Long id);

    Schedule findSpecialLectureWithSchedules(SpecialLecture specialLecture, LocalDate specialLectureDate);
}
