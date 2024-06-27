package lecture.special.domain.service;

import lecture.special.domain.repository.ScheduleRepository;
import lecture.special.domain.repository.SpecialLectureHistoryRepository;
import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.domain.service.business.SpecialLectureDomain;
import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.infra.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialLectureService {

    private final SpecialLectureRepository specialLectureRepository;
    private final ScheduleRepository scheduleRepository;
    private final SpecialLectureHistoryRepository historyRepository;
    private final SpecialLectureDomain domain;

    public void apply(Long userId, String speLecName) {

        //사용자 조회 및 null 검증
        User user = domain.getUser(userId);

        //특강 및 일정 조회
        SpecialLecture specialLecture = specialLectureRepository.findBySpeLecName(speLecName)
                .orElseThrow(() -> new NoSuchElementException("해당하는 특강이 없습니다."));;
        Schedule schedule = scheduleRepository.findBySpecialLecture(specialLecture)
                .orElseThrow(() -> new NoSuchElementException("해당하는 특강이 없습니다."));

        //특강 히스토리 저장
        historyRepository.save(new SpecialLectureHistory(user, schedule));
    }

    public List<SpecialLecture> search() {
        return specialLectureRepository.findAll();
    }

    public void searchUserEnrolled(Long userId, String speLecName) {

        //사용자 조회 및 null 검증
        User user = domain.getUser(userId);

        //해당 이름에 부합하는 특강 데이터 가져오기
        SpecialLecture specialLecture = specialLectureRepository.findBySpeLecName(speLecName)
                .orElseThrow(() -> new NoSuchElementException("해당하는 특강이 없습니다."));;
        //특강 일정에 있는 `특강 id`와 부합하는 `특강 일정 id` 가져오기
        Schedule schedule = scheduleRepository.findBySpecialLecture(specialLecture)
                .orElseThrow(() -> new NoSuchElementException("해당하는 특강이 없습니다."));;

        //히스토리 내역에 (특강 id + 사용자 id)가 없으면 특강 신청에 실패한 사용자
        historyRepository.findByUserAndSchedule(user, schedule)
                .orElseThrow(() -> new IllegalArgumentException(userId + "님은 " + speLecName + " 특강 신청에 실패하였습니다."));
    }
}
