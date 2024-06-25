package lecture.special.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class SpecialLectureController {

    @PostMapping("/apply")
    void apply() {

    }
}
