package lecture.special.domain.service;

import lecture.special.domain.repository.ScheduleRepository;
import lecture.special.domain.repository.SpecialLectureHistoryRepository;
import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.domain.repository.UserRepository;
import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.infra.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialLectureService {

    private final UserRepository userRepository;
    private final SpecialLectureRepository specialLectureRepository;
    private final ScheduleRepository scheduleRepository;
    private final SpecialLectureHistoryRepository historyRepository;

    private User getUser(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) throw new IllegalArgumentException("사용자가 없습니다.");
        return user;
    }

    // ---------------------------------------------------------------------------

    public void apply(Long userId, String speLecName) {
        //사용자 조회
        User user = getUser(userId);

        //특강 및 일정 조회
        SpecialLecture specialLecture = specialLectureRepository.findBySpeLecName(speLecName);
        Schedule schedule = scheduleRepository.findBySpecialLecture(specialLecture);
        if (specialLecture == null && schedule == null) throw new IllegalArgumentException("해당하는 특강이 없습니다.");

        //특강 히스토리 저장
//        SpecialLectureHistory specialLectureHistory = new SpecialLectureHistory(user, schedule);
//        historyRepository.save(specialLectureHistory);

        //특강 신청 여부 완료로 변경
//        user.setTrueEnrolled();
    }

//    public List<SpecialLecture> search() {
//        return specialLectureRepository.findAll();
//    }

//    public void searchUserEnrolled(Long userId) {
//        User user = getUser(userId);
//        if (!user.is_enrolled()) throw new IllegalStateException(userId + "님은 특강 신청에 실패하였습니다.");
//    }
}
