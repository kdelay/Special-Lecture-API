package lecture.special.interfaces;

import java.time.LocalDate;

public record SearchResponse(
        Long specialLectureId,
        String specialLectureName,
        Long scheduleId,
        int capacityCount,
        int enrollCount,
        LocalDate specialLectureDate
) {
}
