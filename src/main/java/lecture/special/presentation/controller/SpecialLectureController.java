package lecture.special.presentation.controller;

import lecture.special.application.service.SpecialLectureService;
import lecture.special.presentation.request.SpecialLectureReq;
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
    public ResponseEntity<String> apply(@RequestBody SpecialLectureReq specialLectureReq) {
        specialLectureService.apply(specialLectureReq);
        return ResponseEntity.ok("특강 신청에 성공하였습니다.");
    }
}
