package lecture.special.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lecture.special.domain.repository.SpecialLectureHistoryRepository;
import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.domain.service.business.SpecialLectureDomain;
import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.infra.entity.mapper.dto.SpecialLectureWithScheduleDTO;
import lecture.special.infra.entity.user.User;
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
    private final SpecialLectureDomain domain;

    public void apply(Long userId, String speLecName, LocalDate speLecDate) {

        //사용자 조회 및 null 검증
        User user = domain.getUser(userId);

        //특강 및 일정 조회
        SpecialLecture specialLecture = domain.getSpecialLecture(speLecName);
        Schedule schedule = domain.getSchedule(specialLecture, speLecDate);

        //더 이상 특강 인원을 받을 수 없는 경우 exception (요청 시 수용 인원 초과)
        boolean isFullCapacity = schedule.getEnrollCount() >= schedule.getCapacityCount();
        if (isFullCapacity) throw new IllegalStateException("수용 인원이 부족하여 더 이상 특강을 신청할 수 없습니다.");

        //특강 신청에 성공하면 schedule 의 신청 인원 수가 증가해야한다.
        schedule.plusEnrollCount();

        //특강 히스토리 저장
        historyRepository.save(new SpecialLectureHistory(user, schedule));
    }

    public List<SpecialLectureWithScheduleDTO> search() {
        return specialLectureRepository.findSpecialLectureWithSchedules();
    }

    public void searchUserEnrolled(Long userId, String speLecName, LocalDate speLecDate) {

        //사용자 조회 및 null 검증
        User user = domain.getUser(userId);

        //해당 이름에 부합하는 특강 데이터 가져오기
        SpecialLecture specialLecture = domain.getSpecialLecture(speLecName);

        //특강 일정에 있는 `특강 id`와 부합하는 `특강 일정 id` 가져오기
        Schedule schedule = domain.getSchedule(specialLecture, speLecDate);

        //히스토리 내역에 (특강 id + 사용자 id)가 없으면 특강 신청에 실패한 사용자
        historyRepository.findByUserAndSchedule(user, schedule)
                .orElseThrow(() -> new IllegalStateException(userId + "님은 " + speLecName + " 특강 신청에 실패하였습니다."));
    }
}
