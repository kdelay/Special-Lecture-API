package lecture.special.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lecture.special.domain.repository.ScheduleRepository;
import lecture.special.domain.repository.SpecialLectureHistoryRepository;
import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.domain.repository.UserRepository;
import lecture.special.domain.service.SpecialLectureService;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.presentation.request.ApplyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SpecialLectureController.class)
class SpecialLectureControllerTest {

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
        ApplyRequest applyReq = new ApplyRequest(userId, speLecName);
        String req = objectMapper.writeValueAsString(applyReq);

        //when & then
        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isOk());

        //service call 검증
        verify(specialLectureService).apply(eq(userId), eq(speLecName));
    }

    @Test
    @DisplayName("GET /lectures 특강 목록 조회")
    void searchTest() throws Exception {

        SpecialLecture lecture1 = new SpecialLecture(1L, "자바");
        SpecialLecture lecture2 = new SpecialLecture(2L, "자바");
        List<SpecialLecture> list = Arrays.asList(lecture1, lecture2);

        when(specialLectureService.search()).thenReturn(list);

        mockMvc.perform(get("/lectures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /lectures/application/{userId} 특강 신청 완료 여부 조회")
    void searchUserEnrolledTest() throws Exception {

        Long userId = 1L;
        String speLecName = "자바";

        doNothing().when(specialLectureService).searchUserEnrolled(anyLong(), anyString());

        mockMvc.perform(get("/lectures/application/{userId}", userId)
                        .param("speLecName", speLecName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //1번 호출 되었는지 검증
        verify(specialLectureService).searchUserEnrolled(eq(userId), eq(speLecName));
    }

}