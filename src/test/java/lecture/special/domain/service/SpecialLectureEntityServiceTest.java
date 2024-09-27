package lecture.special.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lecture.special.domain.*;
import lecture.special.domain.interfaces.ScheduleRepository;
import lecture.special.domain.interfaces.SpecialLectureHistoryRepository;
import lecture.special.domain.interfaces.SpecialLectureRepository;
import lecture.special.domain.interfaces.UserRepository;
import lecture.special.interfaces.ApplyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecialLectureEntityServiceTest {

    @InjectMocks
    SpecialLectureService specialLectureService;

    @Mock
    SpecialLectureRepository specialLectureRepository;

    @Mock
    SpecialLectureHistoryRepository historyRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    // ---------------------------------------------------------------------------

    //유저 초기 데이터 init
    private User whenUser(Long userId) {
        User user = new User(userId);
        lenient().when(userRepository.findById(userId)).thenReturn(user);
        return user;
    }

    //특강 초기 데이터 init
    private SpecialLecture whenSpecialLecture(Long id, String speLecName) {
        return new SpecialLecture(id, speLecName);
    }

    //특강 일정 초기 데이터 init
    private Schedule whenSchedule(Long id, SpecialLecture specialLecture, int capacity, int enroll, LocalDate speLecDate) {
        return new Schedule(id, specialLecture, capacity, enroll, speLecDate);
    }

    //특강 신청 시, request
    private ApplyRequest initSpecialLectureReq(Long userId, String speLecName, LocalDate speLecDate) {
        return new ApplyRequest(userId, speLecName, speLecDate);
    }

    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("특강 신청 성공")
    void applyTest() {

        Long pk = 1L;

        //유저
        User user = whenUser(pk);
        when(userRepository.findById(user.getUserId())).thenReturn(user);

        //특강
        LocalDate specialLectureDate = LocalDate.parse("2024-06-28");
        String specialLectureName = "자바";

        SpecialLecture specialLecture = new SpecialLecture(pk, specialLectureName);
        when(specialLectureRepository.findBySpecialLectureName(specialLectureName)).thenReturn(specialLecture);

        int capacity = 1, enroll = 0;
        Schedule schedule = new Schedule(pk, specialLecture, capacity, enroll, specialLectureDate);
        when(scheduleRepository.findSpecialLectureWithSchedules(specialLecture, specialLectureDate)).thenReturn(schedule);

        //현재 신청 인원은 0명이다.
        assertThat(schedule.getEnrollCount()).isEqualTo(0);

        //특강 신청
        ApplyRequest req = initSpecialLectureReq(user.getUserId(), specialLecture.getSpecialLectureName(), specialLectureDate);
        specialLectureService.apply(req.userId(), req.specialLectureName(), req.specialLectureDate());

        //특강 신청에 성공하면 schedule 의 신청 인원 수가 증가해야한다.
        assertThat(schedule.getEnrollCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자가 없는 경우")
    void isEmptyUserTest() {

        //유저
        Long pk = 1L;
        when(userRepository.findById(pk)).thenThrow(new NoSuchElementException("사용자가 없습니다."));

        //특강
        String speLecName = "자바";
        whenSpecialLecture(pk, speLecName);

        //검증
        assertThatThrownBy(() ->
                specialLectureService.apply(pk, speLecName, LocalDate.ofEpochDay(anyLong())))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("사용자가 없습니다.");
    }

    @Test
    @DisplayName("특강이 없는 경우")
    void isEmptySpecialLectureTest() {

        Long pk = 1L;

        //유저
        User user = whenUser(pk);
        when(userRepository.findById(pk)).thenReturn(user);

        //특강
        String speLecName = null;
        when(specialLectureRepository.findBySpecialLectureName(speLecName)).thenThrow(new NoSuchElementException("해당하는 특강이 없습니다."));

        assertThatThrownBy(() -> specialLectureService.apply(user.getUserId(), speLecName, LocalDate.ofEpochDay(anyLong())))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 특강이 없습니다.");
    }

    @Test
    @DisplayName("더 이상 특강 인원을 받을 수 없는 경우(요청 시 수용 인원 초과)")
    void enrollOverCapacityFailTest() {

        Long pk = 1L;

        //유저
        User user = whenUser(pk);
        when(userRepository.findById(pk)).thenReturn(user);

        //특강
        LocalDate speLecDate = LocalDate.parse("2024-06-28");
        String speLecName = "자바";

        SpecialLecture specialLecture = new SpecialLecture(pk, speLecName);
        when(specialLectureRepository.findBySpecialLectureName(speLecName)).thenReturn(specialLecture);

        //수용 인원이 부족해서 더 이상 특강을 신청할 수 없다고 가정한다.
        int capacity = 1, enroll = 1;
        Schedule schedule = new Schedule(pk, specialLecture, capacity, enroll, speLecDate);
        when(scheduleRepository.findSpecialLectureWithSchedules(specialLecture, speLecDate)).thenReturn(schedule);

        //특강 신청
        ApplyRequest req = initSpecialLectureReq(user.getUserId(), specialLecture.getSpecialLectureName(), speLecDate);

        //특강을 신청할 수 없다.
        assertThatThrownBy(() -> specialLectureService.apply(req.userId(), req.specialLectureName(), req.specialLectureDate()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("수용 인원이 부족하여 더 이상 특강을 신청할 수 없습니다.");
    }

    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("특강 목록 조회 성공")
    void searchTest() throws JsonProcessingException {

        Long specialLectureId = 1L, scheduleId = 1L;

        String name1 = "자바";
        SpecialLecture lecture1 = new SpecialLecture(specialLectureId++, name1);
        Schedule schedule1 = new Schedule(scheduleId++, lecture1, 2, 0, LocalDate.parse("2024-06-26"));
        Schedule schedule2 = new Schedule(scheduleId++, lecture1, 30, 0, LocalDate.parse("2024-06-29"));

        //특강 일정이 모두 동일한 특강을 가지고 있는지 검증
        assertThat(schedule1.getSpecialLecture().getSpecialLectureName()).isEqualTo("자바");
        assertThat(schedule2.getSpecialLecture().getSpecialLectureName()).isEqualTo("자바");

        String name2 = "스프링";
        SpecialLecture lecture2 = new SpecialLecture(specialLectureId, name2);
        Schedule schedule3 = new Schedule(scheduleId++, lecture2, 1, 0, LocalDate.parse("2024-06-25"));
        Schedule schedule4 = new Schedule(scheduleId, lecture2, 5, 0, LocalDate.parse("2024-06-28"));

        //특강 일정이 모두 동일한 특강을 가지고 있는지 검증
        assertThat(schedule3.getSpecialLecture().getSpecialLectureName()).isEqualTo("스프링");
        assertThat(schedule4.getSpecialLecture().getSpecialLectureName()).isEqualTo("스프링");

        //데이터 하나 담기
        List<SpecialLecture> list = List.of(lecture1, lecture2);

        //2개의 list를 가지고 있는지 검증
        assertThat(list).hasSize(2);
    }

    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("특강 신청 성공 조회")
    void searchUserEnrolledTest() {

        Long pk = 1L;

        //유저
        User user = whenUser(pk);
        when(userRepository.findById(pk)).thenReturn(user);

        //특강
        String name = "자바";
        LocalDate date = LocalDate.parse("2024-06-28");
        SpecialLecture specialLecture = whenSpecialLecture(pk, name);
        when(specialLectureRepository.findBySpecialLectureName(name)).thenReturn(specialLecture);

        Schedule schedule = whenSchedule(pk, specialLecture, 2, 0, date);
        when(scheduleRepository.findSpecialLectureWithSchedules(specialLecture, date)).thenReturn(schedule);

        //특강 신청 성공한 경우
        specialLectureService.apply(pk, name, date);

        //특강 히스토리
        SpecialLectureHistory history = new SpecialLectureHistory(user, schedule);

        //유저가 특강 신청에 성공했는지 검증
        assertThat(history.getUser().getUserId()).isEqualTo(1L);
        assertThat(history.getSchedule().getSpecialLecture().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특강 신청 실패 조회")
    void searchUserEnrolledFailTest() {
        Long pk = 1L;
        String name = "자바";
        LocalDate date = LocalDate.parse("2024-06-28");
        String message = pk + "님은 " + name + " 특강 신청에 실패하였습니다.";

        // 유저 조회
        User user = whenUser(pk);

        // 특강 조회
        SpecialLecture specialLecture = whenSpecialLecture(pk, name);
        Schedule schedule = whenSchedule(pk, specialLecture, 2, 0, date);

        // scheduleRepository에 대한 stubbing
        when(scheduleRepository.findSpecialLectureWithSchedules(
                ArgumentMatchers.eq(specialLecture), ArgumentMatchers.eq(date)))
                .thenReturn(schedule);

        // historyRepository에 대한 stubbing
        when(historyRepository.findByUserAndSchedule(
                ArgumentMatchers.eq(user), ArgumentMatchers.eq(schedule)))
                .thenThrow(new IllegalStateException(message));

        // 예외 검증
        assertThatThrownBy(() -> specialLectureService.searchUserEnrolled(pk, name, date))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(message);
    }
}