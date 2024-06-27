package lecture.special.presentation.controller;

import lecture.special.domain.service.SpecialLectureService;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.presentation.request.ApplyRequest;
import lecture.special.presentation.response.ApplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class SpecialLectureController {

    private final SpecialLectureService specialLectureService;

    @PostMapping("/apply")
    public ResponseEntity<ApplyResponse> apply(@RequestBody ApplyRequest request) {

        Long userId = request.userId();
        String speLecName = request.speLecName();

        specialLectureService.apply(userId, speLecName);

        ApplyResponse response = new ApplyResponse(speLecName + " 특강 신청에 성공하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public List<SpecialLecture> search() {
        return specialLectureService.search();
    }

//    @GetMapping("/application/{userId}")
//    public ResponseEntity<SpecialLectureRes> searchUserEnrolled(@PathVariable Long userId) {
//        specialLectureService.searchUserEnrolled(userId);
//
//        SpecialLectureRes response = new SpecialLectureRes(userId + "님은 특강 신청에 성공하였습니다.");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
