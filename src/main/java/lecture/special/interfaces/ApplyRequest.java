package lecture.special.interfaces;

import java.time.LocalDate;

public record ApplyRequest(
        Long userId,
        String specialLectureName,
        LocalDate specialLectureDate
) {
}
