package lecture.special.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lecture.special.domain.Schedule;
import lecture.special.domain.SpecialLecture;
import lecture.special.domain.SpecialLectureService;
import lecture.special.domain.interfaces.ScheduleRepository;
import lecture.special.domain.interfaces.SpecialLectureHistoryRepository;
import lecture.special.domain.interfaces.SpecialLectureRepository;
import lecture.special.domain.interfaces.UserRepository;
import lecture.special.interfaces.ApplyRequest;
import lecture.special.interfaces.SpecialLectureController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SpecialLectureController.class)
class SpecialLectureEntityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SpecialLectureService specialLectureService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    SpecialLectureRepository specialLectureRepository;

    @MockBean
    ScheduleRepository scheduleRepository;

    @MockBean
    SpecialLectureHistoryRepository historyRepository;

    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("POST /lectures/apply 특강 신청")
    void applyTest() throws Exception {

        //given
        Long userId = 1L;
        String speLecName = "자바";
        LocalDate speLecDate = LocalDate.parse("2024-06-28");
        ApplyRequest applyReq = new ApplyRequest(userId, speLecName, speLecDate);
        String req = objectMapper.writeValueAsString(applyReq);

        //when & then
        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isOk());

        //service call 검증
        verify(specialLectureService).apply(eq(userId), eq(speLecName), eq(speLecDate));
    }

    @Test
    @DisplayName("GET /lectures 특강 목록 조회")
    void searchTest() throws Exception {

        //Mock 데이터 설정
        Schedule schedule1 = new Schedule(1L, null, 3, 1, LocalDate.parse("2024-06-28"));
        Schedule schedule2 = new Schedule(2L, null, 2, 0, LocalDate.parse("2024-06-28"));
        SpecialLecture specialLecture1 = new SpecialLecture(1L, "자바", List.of(schedule1, schedule2));
        SpecialLecture specialLecture2 = new SpecialLecture(2L, "자바", List.of(schedule1, schedule2));
        List<SpecialLecture> list = List.of(specialLecture1, specialLecture2);

        //Mock 서비스 메서드 설정
        when(specialLectureService.search()).thenReturn(list);

        mockMvc.perform(get("/lectures")
                        .contentType(MediaType.APPLICATION_JSON)) //Content-Type 설정
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /lectures/application/{userId} 특강 신청 완료 여부 조회")
    void searchUserEnrolledTest() throws Exception {

        Long userId = 1L;
        String name = "자바";
        LocalDate date = LocalDate.parse("2024-06-28");

        doNothing().when(specialLectureService).searchUserEnrolled(userId, name, date);

        mockMvc.perform(get("/lectures/application/{userId}", userId)
                        .param("specialLectureName", name)
                        .param("specialLectureDate", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //1번 호출 되었는지 검증
        verify(specialLectureService).searchUserEnrolled(eq(userId), eq(name), eq(date));
    }
}