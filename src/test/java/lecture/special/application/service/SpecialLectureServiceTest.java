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

    private static SpecialLectureReq getSpecialLectureReq(Long userId, String speLecName) {
        return new SpecialLectureReq(userId, speLecName);
    }

    // ---------------------------------------------------------------------------

    /**
     * public void apply(SpecialLectureReq specialLectureReq) {...}
     */

    @Test
    @DisplayName("특강 신청 성공")
    void apply() {

        //유저
        Long userId = 1L;
        User user = whenUser(userId);

        //특강
        Long id = 1L;
        String speLecName = "자바";
        int capacity = 1;
        LocalDate localDate = LocalDate.parse("2024-06-25");
        whenSpecialLecture(id, speLecName, capacity, localDate);

        //특강 신청
        specialLectureService.apply(getSpecialLectureReq(userId, speLecName));
        //save 메서드가 호출되었는지 검증
        verify(historyRepository).save(any(SpecialLectureHistory.class));

        //특강 신청 여부 완료로 변경
        user.setTrueEnrolled();
        //user의 is_enrolled가 true로 변경되었는지 검증
        assertThat(user.is_enrolled()).isEqualTo(true);
    }

    @Test
    @DisplayName("사용자가 없는 경우")
    void isEmptyUser() {

        //유저
        Long userId = null;
        when(userRepository.findById(userId)).thenReturn(null);

        //특강
        Long id = 1L;
        String speLecName = "자바";
        int capacity = 1;
        LocalDate localDate = LocalDate.parse("2024-06-25");
        whenSpecialLecture(id, speLecName, capacity, localDate);

        assertThatThrownBy(() -> specialLectureService.apply(getSpecialLectureReq(userId, speLecName)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자가 없습니다.");
    }

    @Test
    @DisplayName("특강이 없는 경우")
    void isEmptySpecialLecture() {

        //유저
        Long userId = 1L;
        whenUser(userId);

        //특강
        String speLecName = null;
        when(specialLectureRepository.findBySpeLecName(speLecName)).thenReturn(null);

        assertThatThrownBy(() -> specialLectureService.apply(getSpecialLectureReq(userId, speLecName)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 특강이 없습니다.");
    }

    // ---------------------------------------------------------------------------

    /**
     * public List<SpecialLecture> search() {...}
     */

    @Test
    @DisplayName("특강 목록 조회 성공")
    void search() {

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
}