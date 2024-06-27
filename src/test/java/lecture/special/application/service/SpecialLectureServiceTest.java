package lecture.special.application.service;

import lecture.special.domain.repository.ScheduleRepository;
import lecture.special.domain.repository.SpecialLectureHistoryRepository;
import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.domain.repository.UserRepository;
import lecture.special.domain.service.SpecialLectureService;
import lecture.special.domain.service.business.SpecialLectureDomain;
import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.lecture.SpecialLectureHistory;
import lecture.special.infra.entity.user.User;
import lecture.special.presentation.request.ApplyRequest;
import org.junit.jupiter.api.*;
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
        when(specialLectureRepository.findBySpeLecName(speLecName)).thenReturn(specialLecture);
        return specialLecture;
    }

    //특강 일정 초기 데이터 init
    private Schedule whenSchedule(Long id, SpecialLecture specialLecture, int capacity, int enroll, LocalDate speLecDate) {
        Schedule schedule = new Schedule(id, specialLecture, capacity, enroll, speLecDate);
        when(scheduleRepository.findById(id)).thenReturn(schedule);
        return schedule;
    }

    //특강 신청 시, request
    private ApplyRequest initSpecialLectureReq(Long userId, String speLecName) {
        return new ApplyRequest(userId, speLecName);
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
        SpecialLecture specialLecture = whenSpecialLecture(pk, "자바");
        whenSchedule(pk, specialLecture, 30, 0, LocalDate.of(2024, 6, 27));

        //특강 신청
        ApplyRequest req = initSpecialLectureReq(user.getUserId(), specialLecture.getSpeLecName());
        specialLectureService.apply(req.userId(), req.speLecName());

        //save 메서드가 호출되었는지 검증
        verify(historyRepository).save(any(SpecialLectureHistory.class));
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
                specialLectureService.apply(pk, speLecName))
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
        when(specialLectureRepository.findBySpeLecName(speLecName)).thenReturn(null);

        assertThatThrownBy(() -> specialLectureService.apply(user.getUserId(), speLecName))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 특강이 없습니다.");
    }

    // ---------------------------------------------------------------------------

    /**
     * public List<SpecialLecture> search() {...}
     */

    @Test
    @DisplayName("특강 목록 조회 성공")
    void searchTest() {

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

        List<SpecialLecture> list = Arrays.asList(lecture1, lecture2);

        when(specialLectureRepository.findAll()).thenReturn(list);

        List<SpecialLecture> search = specialLectureService.search();

        //2개의 list를 가지고 있는지 검증
        assertThat(search).hasSize(2);

        //id가 1L, 2L 가 있는지 검증
        assertThat(search).extracting(SpecialLecture::getId).containsExactlyInAnyOrder(1L, 2L);
    }

    // ---------------------------------------------------------------------------

    /**
     * public ResponseEntity<SpecialLectureRes> searchUserEnrolled(@PathVariable Long userId) {...}
     */

    @Test
    @Disabled
    @DisplayName("특강 신청 성공")
    void searchUserEnrolledTest() {

//        //유저
//        Long userId = 1L;
//        User user = getUser();
//
//        //특강
//        SpecialLecture specialLecture = getSpecialLecture();
//
//        //특강 신청 성공한 경우
//        specialLectureService.apply(getSpecialLectureReq(userId, specialLecture.getSpeLecName()));
//
//        //특강 히스토리 저장 메서드가 호출되었는지 검증
//        verify(historyRepository).save(any(SpecialLectureHistory.class));
//
//        //특강 히스토리에 유저 아이디가 있는지 검증
////        when(historyRepository.findByUser(user))
//
//        //특강 신청 여부가 완료로 변경되었는지 검증
//        user.setTrueEnrolled();
//        assertThat(user.is_enrolled()).isEqualTo(true);
//
//        specialLectureService.searchUserEnrolled(userId);
    }

    @Test
    @Disabled
    @DisplayName("특강 신청 실패")
    void searchUserEnrolledFailTest() {

//        //유저
//        Long userId = 1L;
//        getUser();
//
//        //특강
//        getSpecialLecture();
//
//        assertThatThrownBy(() -> specialLectureService.searchUserEnrolled(userId))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessage(userId + "님은 특강 신청에 실패하였습니다.");
    }
}