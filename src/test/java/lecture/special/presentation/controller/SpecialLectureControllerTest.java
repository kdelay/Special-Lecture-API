package lecture.special.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lecture.special.application.service.SpecialLectureService;
import lecture.special.presentation.request.SpecialLectureReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SpecialLectureController.class)
class SpecialLectureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SpecialLectureService specialLectureService;

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

}