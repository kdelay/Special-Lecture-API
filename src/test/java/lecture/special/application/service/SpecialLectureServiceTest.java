package lecture.special.application.service;

import lecture.special.domain.model.lecture.SpecialLecture;
import lecture.special.domain.model.lecture.SpecialLectureRepository;
import lecture.special.domain.model.lecture.history.SpecialLectureHistory;
import lecture.special.domain.model.lecture.history.SpecialLectureHistoryRepository;
import lecture.special.domain.model.user.User;
import lecture.special.domain.model.user.UserRepository;
import lecture.special.presentation.request.SpecialLectureReq;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@Transactional
class SpecialLectureServiceTest {

    @InjectMocks
    SpecialLectureService specialLectureService;

    @Mock
    SpecialLectureRepository specialLectureRepository;

    @Mock
    SpecialLectureHistoryRepository historyRepository;

    @Mock
    UserRepository userRepository;

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

    private SpecialLecture whenSpecialLecture(Long id, String speLecName, int capacity, LocalDate localDate) {
        SpecialLecture specialLecture = new SpecialLecture(id, speLecName, capacity, localDate);
        when(specialLectureRepository.findBySpeLecName(speLecName)).thenReturn(specialLecture);
        return specialLecture;
    }

    private User whenUser(Long userId) {
        User user = new User(userId);
        when(userRepository.findById(userId)).thenReturn(user);
        return user;
    }

    private SpecialLectureReq getSpecialLectureReq(Long userId, String speLecName) {
        return new SpecialLectureReq(userId, speLecName);
    }

    private User getUser() { return whenUser(1L); }

    private SpecialLecture getSpecialLecture() {
        return whenSpecialLecture(1L, "자바", 1, LocalDate.parse("2024-06-25"));
    }

    // ---------------------------------------------------------------------------

    /**
     * public void apply(SpecialLectureReq specialLectureReq) {...}
     */

    @Test
    @DisplayName("특강 신청 성공")
    void applyTest() {

        //유저
        User user = getUser();

        //특강
        SpecialLecture specialLecture = getSpecialLecture();

        //특강 신청
        specialLectureService.apply(getSpecialLectureReq(user.getUserId(), specialLecture.getSpeLecName()));
        //save 메서드가 호출되었는지 검증
        verify(historyRepository).save(any(SpecialLectureHistory.class));

        //특강 신청 여부 완료로 변경
        user.setTrueEnrolled();
        //user의 is_enrolled가 true로 변경되었는지 검증
        assertThat(user.is_enrolled()).isEqualTo(true);
    }

    @Test
    @DisplayName("사용자가 없는 경우")
    void isEmptyUserTest() {

        //유저
        Long userId = null;
        when(userRepository.findById(userId)).thenReturn(null);

        //특강
        SpecialLecture specialLecture = getSpecialLecture();

        assertThatThrownBy(() ->
                specialLectureService.apply(getSpecialLectureReq(userId, specialLecture.getSpeLecName())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자가 없습니다.");
    }

    @Test
    @DisplayName("특강이 없는 경우")
    void isEmptySpecialLectureTest() {

        //유저
        User user = getUser();

        //특강
        String speLecName = null;
        when(specialLectureRepository.findBySpeLecName(speLecName)).thenReturn(null);

        assertThatThrownBy(() -> specialLectureService.apply(getSpecialLectureReq(user.getUserId(), speLecName)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 특강이 없습니다.");
    }

    // ---------------------------------------------------------------------------

    /**
     * public List<SpecialLecture> search() {...}
     */

    @Test
    @DisplayName("특강 목록 조회 성공")
    void searchTest() {

        LocalDate localDate = LocalDate.parse("2024-06-26");
        SpecialLecture lecture1 = new SpecialLecture(1L, "자바", 1, localDate);
        SpecialLecture lecture2 = new SpecialLecture(2L, "스프링", 2, localDate);
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
     * public ResponseEntity<String> searchUserEnrolled(@PathVariable Long userId) {...}
     */

    @Test
    @DisplayName("특강 신청 성공")
    void searchUserEnrolledTest() {

        //유저
        Long userId = 1L;
        User user = getUser();

        //특강
        SpecialLecture specialLecture = getSpecialLecture();

        //특강 신청 성공한 경우
        specialLectureService.apply(getSpecialLectureReq(userId, specialLecture.getSpeLecName()));

        //특강 신청 여부가 완료로 변경되었는지 검증
        user.setTrueEnrolled();
        assertThat(user.is_enrolled()).isEqualTo(true);

        specialLectureService.searchUserEnrolled(userId);
    }

    @Test
    @DisplayName("특강 신청 실패")
    void searchUserEnrolledFailTest() {

        //유저
        Long userId = 1L;
        getUser();

        //특강
        getSpecialLecture();

        assertThatThrownBy(() -> specialLectureService.searchUserEnrolled(userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(userId + "님은 특강 신청에 실패하였습니다.");
    }
}