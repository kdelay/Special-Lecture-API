package lecture.special.interfaces;

import lecture.special.domain.SpecialLectureService;
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
        String specialLectureName = request.specialLectureName();
        LocalDate specialLectureDate = request.specialLectureDate();

        specialLectureService.apply(userId, specialLectureName, specialLectureDate);

        MessageResponse response = new MessageResponse(specialLectureDate + "날에 진행하는 " +
                specialLectureName + " 특강 신청에 성공하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public List<SearchResponse> search() {
        return specialLectureService.search().stream()
                .flatMap(lecture -> lecture.getSchedules().stream()
                        .map(schedule -> new SearchResponse(
                                lecture.getId(),
                                lecture.getSpecialLectureName(),
                                schedule.getId(),
                                schedule.getCapacityCount(),
                                schedule.getEnrollCount(),
                                schedule.getSpecialLectureDate()
                        ))
                ).toList();
    }

    @GetMapping("/application/{userId}")
    public ResponseEntity<MessageResponse> searchUserEnrolled(
            @PathVariable Long userId,
            @RequestParam("specialLectureName") String speLecName,
            @RequestParam("specialLectureDate") LocalDate speLecDate
    ) {
        specialLectureService.searchUserEnrolled(userId, speLecName, speLecDate);

        MessageResponse response = new MessageResponse(userId + "님은 " + speLecDate + "날에 진행하는 " +
                speLecName + " 특강 신청에 성공하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
