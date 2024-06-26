package lecture.special.application.service;

import lecture.special.domain.model.lecture.SpecialLecture;
import lecture.special.domain.model.lecture.SpecialLectureRepository;
import lecture.special.domain.model.lecture.history.SpecialLectureHistory;
import lecture.special.domain.model.lecture.history.SpecialLectureHistoryRepository;
import lecture.special.domain.model.user.User;
import lecture.special.domain.model.user.UserRepository;
import lecture.special.presentation.request.SpecialLectureReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialLectureService {

    private final UserRepository userRepository;
    private final SpecialLectureRepository specialLectureRepository;
    private final SpecialLectureHistoryRepository historyRepository;

    private User getUser(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) throw new IllegalArgumentException("사용자가 없습니다.");
        return user;
    }

    // ---------------------------------------------------------------------------

    public void apply(SpecialLectureReq specialLectureReq) {
        //사용자 조회
        User user = getUser(specialLectureReq.userId());

        //특강 조회
        SpecialLecture specialLecture = specialLectureRepository.findBySpeLecName(specialLectureReq.speLecName());
        if (specialLecture == null) throw new IllegalArgumentException("해당하는 특강이 없습니다.");

        //특강 히스토리 저장
        SpecialLectureHistory specialLectureHistory = new SpecialLectureHistory(user, specialLecture);
        historyRepository.save(specialLectureHistory);

        //특강 신청 여부 완료로 변경
        user.setTrueEnrolled();
    }

    public List<SpecialLecture> search() {
        return specialLectureRepository.findAll();
    }

    public void searchUserEnrolled(Long userId) {
        User user = getUser(userId);
        if (!user.is_enrolled()) throw new IllegalStateException(userId + "님은 특강 신청에 실패하였습니다.");
    }
}
