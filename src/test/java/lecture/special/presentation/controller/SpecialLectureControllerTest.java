package lecture.special.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lecture.special.application.service.SpecialLectureService;
import lecture.special.domain.model.lecture.SpecialLecture;
import lecture.special.domain.model.user.User;
import lecture.special.domain.model.user.UserRepository;
import lecture.special.presentation.request.SpecialLectureReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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

    // ---------------------------------------------------------------------------

    @Test
    @DisplayName("POST /lectures/apply 특강 신청")
    void apply() throws Exception {

        Long userId = 1L;
        String spcLecName = "자바";
        SpecialLectureReq specialLectureReq = new SpecialLectureReq(userId, spcLecName);
        String req = objectMapper.writeValueAsString(specialLectureReq);

        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isOk())
                .andExpect(content().string("특강 신청에 성공하였습니다."));

        verify(specialLectureService, times(1)).apply(specialLectureReq);
    }

    @Test
    @DisplayName("GET /lectures 특강 목록 조회")
    void search() throws Exception {

        LocalDate localDate = LocalDate.parse("2024-06-26");
        SpecialLecture lecture1 = new SpecialLecture(1L, "자바", 1, localDate);
        SpecialLecture lecture2 = new SpecialLecture(1L, "자바", 1, localDate);
        List<SpecialLecture> list = Arrays.asList(lecture1, lecture2);

        when(specialLectureService.search()).thenReturn(list);

        mockMvc.perform(get("/lectures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /lectures/application/{userId} 특강 신청 완료 여부 조회")
    void searchUserEnrolled() throws Exception {

        //유저 조회
        Long userId = 1L;
        User user = new User(userId);
        when(userRepository.findById(userId)).thenReturn(user);

        mockMvc.perform(get("/lectures/application/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(userId + "님은 특강 신청에 성공하였습니다."));

        //1번 호출 되었는지 검증
        verify(specialLectureService, times(1)).searchUserEnrolled(userId);
    }

}