package lecture.special.presentation.controller;

import lecture.special.domain.service.SpecialLectureService;
import lecture.special.presentation.request.ApplyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class SpecialLectureController {

    private final SpecialLectureService specialLectureService;

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody ApplyRequest applyRequest) {

        Long userId = applyRequest.userId();
        String speLecName = applyRequest.speLecName();

        specialLectureService.apply(userId, speLecName);
        return ResponseEntity.ok("특강 신청에 성공하였습니다.");
    }

//    @GetMapping
//    public List<SpecialLecture> search() {
//        return specialLectureService.search();
//    }

//    @GetMapping("/application/{userId}")
//    public ResponseEntity<SpecialLectureRes> searchUserEnrolled(@PathVariable Long userId) {
//        specialLectureService.searchUserEnrolled(userId);
//
//        SpecialLectureRes response = new SpecialLectureRes(userId + "님은 특강 신청에 성공하였습니다.");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
