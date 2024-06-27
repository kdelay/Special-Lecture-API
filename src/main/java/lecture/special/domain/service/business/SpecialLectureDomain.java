package lecture.special.domain.service.business;

import lecture.special.domain.repository.ScheduleRepository;
import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.domain.repository.UserRepository;
import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class SpecialLectureDomain {

    private final UserRepository userRepository;
    private final SpecialLectureRepository specialLectureRepository;
    private final ScheduleRepository scheduleRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자가 없습니다."));
    }

    public SpecialLecture getSpecialLecture(String speLecName) {
        return specialLectureRepository.findBySpeLecName(speLecName)
                .orElseThrow(() -> new NoSuchElementException("해당하는 특강이 없습니다."));
    }

    //특강 일자 조회 및 쓰기 동시성 제어(비관적 락 사용)
    public Schedule getSchedule(SpecialLecture specialLecture, LocalDate speLecDate) {
        return scheduleRepository.findBySpecialLectureAndSpeLecDate(specialLecture, speLecDate)
                .orElseThrow(() -> new NoSuchElementException("해당하는 특강 일자가 없습니다."));
    }
}