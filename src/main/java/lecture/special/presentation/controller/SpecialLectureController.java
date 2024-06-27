package lecture.special.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.server.PathParam;
import lecture.special.domain.service.SpecialLectureService;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.mapper.dto.SpecialLectureWithScheduleDTO;
import lecture.special.presentation.request.ApplyRequest;
import lecture.special.presentation.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class SpecialLectureController {

    private final SpecialLectureService specialLectureService;

    @PostMapping("/apply")
    public ResponseEntity<MessageResponse> apply(@RequestBody ApplyRequest request) {

        Long userId = request.userId();
        String speLecName = request.speLecName();
        LocalDate speLecDate = request.speLecDate();

        specialLectureService.apply(userId, speLecName, speLecDate);

        MessageResponse response = new MessageResponse(speLecDate + "날에 진행하는 " + speLecName + " 특강 신청에 성공하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public List<SpecialLectureWithScheduleDTO> search() {
        return specialLectureService.search();
    }

    @GetMapping("/application/{userId}")
    public ResponseEntity<MessageResponse> searchUserEnrolled(
            @PathVariable Long userId, @PathParam("speLecName") String speLecName, @PathParam("speLecDate") LocalDate speLecDate
    ) {
        specialLectureService.searchUserEnrolled(userId, speLecName, speLecDate);

        MessageResponse response = new MessageResponse(userId + "님은 " + speLecDate + "날에 진행하는 " +speLecName + " 특강 신청에 성공하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
