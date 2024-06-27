package lecture.special.infra.entity.mapper.dto;

import java.time.LocalDate;

public record SpecialLectureWithScheduleDTO(
        Long specialLectureId,
        String speLecName,
        Long scheduleId,
        int capacityCount,
        int enrollCount,
        LocalDate speLecDate
) {
}
