package lecture.special.presentation.controller;

import lecture.special.application.service.SpecialLectureService;
import lecture.special.domain.model.lecture.SpecialLecture;
import lecture.special.presentation.request.SpecialLectureReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<SpecialLecture> search() {
        return specialLectureService.search();
    }
}
