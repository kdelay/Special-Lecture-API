package lecture.special.domain;

import lecture.special.domain.interfaces.ScheduleRepository;
import lecture.special.domain.interfaces.SpecialLectureHistoryRepository;
import lecture.special.domain.interfaces.SpecialLectureRepository;
import lecture.special.domain.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialLectureService {

    private final SpecialLectureRepository specialLectureRepository;
    private final SpecialLectureHistoryRepository historyRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public void apply(Long userId, String specialLectureName, LocalDate specialLectureDate) {
        //사용자 조회
        User user = userRepository.findById(userId);

        //특강 및 일정 조회
        SpecialLecture specialLecture = specialLectureRepository.findBySpecialLectureName(specialLectureName);
        Schedule schedule = scheduleRepository.findSpecialLectureWithSchedules(specialLecture, specialLectureDate);

        //특강 신청에 성공하면 schedule 의 신청 인원 수가 증가해야한다.
        schedule.plusEnrollCount(schedule);

        //특강 히스토리 저장
        historyRepository.save(new SpecialLectureHistory(user, schedule));
    }

    public List<SpecialLecture> search() {
        return specialLectureRepository.findAll();
    }

    public void searchUserEnrolled(Long userId, String specialLectureName, LocalDate specialLectureDate) {
        //사용자 조회
        User user = userRepository.findById(userId);

        //해당 이름에 부합하는 특강 데이터 가져오기
        SpecialLecture specialLecture = specialLectureRepository.findBySpecialLectureName(specialLectureName);

        //특강 일정에 있는 `특강 id`와 부합하는 `특강 일정 id` 가져오기
        Schedule schedule = scheduleRepository.findSpecialLectureWithSchedules(specialLecture, specialLectureDate);

        //히스토리 내역에 (특강 id + 사용자 id)가 없으면 특강 신청에 실패한 사용자
        historyRepository.findByUserAndSchedule(user, schedule);
    }
}
