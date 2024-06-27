package lecture.special.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lecture.special.domain.repository.ScheduleRepository;
import lecture.special.domain.repository.SpecialLectureHistoryRepository;
import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.domain.repository.UserRepository;
import lecture.special.domain.service.SpecialLectureService;
import lecture.special.domain.service.business.SpecialLectureDomain;
import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.infra.entity.mapper.dto.SpecialLectureWithScheduleDTO;
import lecture.special.infra.entity.user.User;
import lecture.special.presentation.request.ApplyRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
class SpecialLectureServiceTest {

    @InjectMocks
    SpecialLectureService specialLectureService;

    @Mock
    SpecialLectureDomain domain;

    @Mock
    SpecialLectureRepository specialLectureRepository;

    @Mock
    SpecialLectureHistoryRepository historyRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    // ---------------------------------------------------------------------------

    //유저 초기 데이터 init
    private User whenUser(Long userId) {
        User user = new User(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        return user;
    }

    //특강 초기 데이터 init
    private SpecialLecture whenSpecialLecture(Long id, String speLecName) {
        SpecialLecture specialLecture = new SpecialLecture(id, speLecName);
        when(specialLectureRepository.findBySpeLecName(speLecName)).thenReturn(Optional.of(specialLecture));
        return specialLecture;
    }

    //특강 일정 초기 데이터 init
    private Schedule whenSchedule(Long id, SpecialLecture specialLecture, int capacity, int enroll, LocalDate speLecDate) {
        Schedule schedule = new Schedule(id, specialLecture, capacity, enroll, speLecDate);
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(schedule));
        return schedule;
    }

    //특강 신청 시, request
    private ApplyRequest initSpecialLectureReq(Long userId, String speLecName, LocalDate speLecDate) {
        return new ApplyRequest(userId, speLecName, speLecDate);
    }

    // ---------------------------------------------------------------------------

    /**
     * public void apply(SpecialLectureReq specialLectureReq) {...}
     */

    @Test
    @DisplayName("특강 신청 성공")
    void applyTest() {

        Long pk = 1L;

        //유저
        User user = whenUser(pk);
        when(domain.getUser(pk)).thenReturn(user);

        //특강
        LocalDate speLecDate = LocalDate.parse("2024-06-28");
        String speLecName = "자바";

        SpecialLecture specialLecture = new SpecialLecture(pk, speLecName);
        when(domain.getSpecialLecture(speLecName)).thenReturn(specialLecture);

        int capacity = 1, enroll = 0;
        Schedule schedule = new Schedule(pk, specialLecture, capacity, enroll, speLecDate);
        when(domain.getSchedule(specialLecture, speLecDate)).thenReturn(schedule);

        //현재 신청 인원은 0명이다.
        assertThat(schedule.getEnrollCount()).isEqualTo(0);

        //특강 신청
        ApplyRequest req = initSpecialLectureReq(user.getUserId(), specialLecture.getSpeLecName(), speLecDate);
        specialLectureService.apply(req.userId(), req.speLecName(), req.speLecDate());

        //save 메서드가 호출되었는지 검증
        verify(historyRepository).save(any(SpecialLectureHistory.class));

        //특강 신청에 성공하면 schedule 의 신청 인원 수가 증가해야한다.
        assertThat(schedule.getEnrollCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자가 없는 경우")
    void isEmptyUserTest() {

        //유저
        Long pk = 1L;
        when(domain.getUser(pk)).thenThrow(new NoSuchElementException("사용자가 없습니다."));

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
        when(domain.getUser(pk)).thenReturn(user);

        //특강
        String speLecName = null;
        when(domain.getSpecialLecture(speLecName)).thenThrow(new NoSuchElementException("해당하는 특강이 없습니다."));

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
        when(domain.getUser(pk)).thenReturn(user);

        //특강
        LocalDate speLecDate = LocalDate.parse("2024-06-28");
        String speLecName = "자바";

        SpecialLecture specialLecture = new SpecialLecture(pk, speLecName);
        when(domain.getSpecialLecture(speLecName)).thenReturn(specialLecture);

        //수용 인원이 부족해서 더 이상 특강을 신청할 수 없다고 가정한다.
        int capacity = 1, enroll = 1;
        Schedule schedule = new Schedule(pk, specialLecture, capacity, enroll, speLecDate);
        when(domain.getSchedule(specialLecture, speLecDate)).thenReturn(schedule);

        //특강 신청
        ApplyRequest req = initSpecialLectureReq(user.getUserId(), specialLecture.getSpeLecName(), speLecDate);

        //특강을 신청할 수 없다.
        assertThatThrownBy(() -> specialLectureService.apply(req.userId(), req.speLecName(), req.speLecDate()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("수용 인원이 부족하여 더 이상 특강을 신청할 수 없습니다.");
    }

    // ---------------------------------------------------------------------------

    /**
     * public List<SpecialLecture> search() {...}
     */

    @Test
    @DisplayName("특강 목록 조회 성공")
    void searchTest() throws JsonProcessingException {

        Long speLecId = 1L, scheduleId = 1L;

        String speLecName1 = "자바";
        SpecialLecture lecture1 = new SpecialLecture(speLecId++, speLecName1);
        Schedule schedule1 = new Schedule(scheduleId++, lecture1, 2, 0, LocalDate.parse("2024-06-26"));
        Schedule schedule2 = new Schedule(scheduleId++, lecture1, 30, 0, LocalDate.parse("2024-06-29"));

        //특강 일정이 모두 동일한 특강을 가지고 있는지 검증
        assertThat(schedule1.getSpecialLecture().getSpeLecName()).isEqualTo("자바");
        assertThat(schedule2.getSpecialLecture().getSpeLecName()).isEqualTo("자바");

        String speLecName2 = "스프링";
        SpecialLecture lecture2 = new SpecialLecture(speLecId, speLecName2);
        Schedule schedule3 = new Schedule(scheduleId++, lecture2, 1, 0, LocalDate.parse("2024-06-25"));
        Schedule schedule4 = new Schedule(scheduleId, lecture2, 5, 0, LocalDate.parse("2024-06-28"));

        //특강 일정이 모두 동일한 특강을 가지고 있는지 검증
        assertThat(schedule3.getSpecialLecture().getSpeLecName()).isEqualTo("스프링");
        assertThat(schedule4.getSpecialLecture().getSpeLecName()).isEqualTo("스프링");

        //데이터 하나 담기
        SpecialLectureWithScheduleDTO dto = new SpecialLectureWithScheduleDTO(speLecId, speLecName1, scheduleId, 2, 0, LocalDate.parse("2024-06-26"));
        List<SpecialLectureWithScheduleDTO> list = Arrays.asList(dto);

        when(specialLectureRepository.findSpecialLectureWithSchedules()).thenReturn(list);

        List<SpecialLectureWithScheduleDTO> search = specialLectureService.search();

        //2개의 list를 가지고 있는지 검증
        assertThat(search).hasSize(1);
    }

    // ---------------------------------------------------------------------------

    /**
     * public ResponseEntity<SpecialLectureRes> searchUserEnrolled(@PathVariable Long userId) {...}
     */

    @Test
    @DisplayName("특강 신청 성공 조회")
    void searchUserEnrolledTest() {

        Long pk = 1L;

        //유저
        User user = whenUser(pk);
        when(domain.getUser(pk)).thenReturn(user);

        //특강
        String speLecName = "자바";
        LocalDate speLecDate = LocalDate.parse("2024-06-28");
        SpecialLecture specialLecture = whenSpecialLecture(pk, speLecName);
        when(domain.getSpecialLecture(speLecName)).thenReturn(specialLecture);

        Schedule schedule = whenSchedule(pk, specialLecture, 2, 0, speLecDate);
        when(domain.getSchedule(specialLecture, speLecDate)).thenReturn(schedule);

        //특강 신청 성공한 경우
        specialLectureService.apply(pk, speLecName, speLecDate);

        //특강 히스토리 저장 메서드가 호출되었는지 검증
        verify(historyRepository).save(any(SpecialLectureHistory.class));

        //특강 히스토리
        SpecialLectureHistory history = new SpecialLectureHistory(user, schedule);
        when(historyRepository.findByUserAndSchedule(user, schedule)).thenReturn(Optional.of(history));

        //유저가 특강 신청에 성공했는지 검증
        assertThat(history.getUser().getUserId()).isEqualTo(1L);
        assertThat(history.getSchedule().getSpecialLecture().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특강 신청 실패 조회")
    void searchUserEnrolledFailTest() {

        Long pk = 1L;
        String speLecName = "자바";
        String message = pk + "님은 " + speLecName + " 특강 신청에 실패하였습니다.";

        //유저 조회
        User user = whenUser(pk);

        //특강 조회
        LocalDate speLecDate = LocalDate.parse("2024-06-28");
        SpecialLecture specialLecture = whenSpecialLecture(pk, speLecName);
        Schedule schedule = whenSchedule(pk, specialLecture, 2, 0, speLecDate);

        //특강 히스토리에 없는 경우 exception 발생
        when(historyRepository.findByUserAndSchedule(user, schedule))
                .thenThrow(new IllegalStateException(message));

        assertThatThrownBy(() -> specialLectureService.searchUserEnrolled(pk, speLecName, speLecDate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(message);
    }
}