package lecture.special.domain;

import lecture.special.infrastructure.db.ScheduleEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Schedule {

    private Long id;
    private SpecialLecture specialLecture;
    private int capacityCount;
    private int enrollCount;
    private LocalDate specialLectureDate;

    public Schedule(Long id, SpecialLecture specialLecture, int capacityCount, int enrollCount, LocalDate specialLectureDate) {
        this.id = id;
        this.specialLecture = specialLecture;
        this.capacityCount = capacityCount;
        this.enrollCount = enrollCount;
        this.specialLectureDate = specialLectureDate;
    }

    public void plusEnrollCount(Schedule schedule) {
        //더 이상 특강 인원을 받을 수 없는 경우 exception (요청 시 수용 인원 초과)
        boolean isFullCapacity = schedule.getEnrollCount() >= schedule.getCapacityCount();
        if (isFullCapacity) throw new IllegalStateException("수용 인원이 부족하여 더 이상 특강을 신청할 수 없습니다.");
        this.enrollCount++;
    }

    public static Schedule toDomain(ScheduleEntity entity) {
        return new Schedule(
                entity.getId(),
                SpecialLecture.toDomain(entity.getSpecialLectureEntity()),
                entity.getCapacityCount(),
                entity.getEnrollCount(),
                entity.getSpecialLectureDate()
        );
    }

    public static ScheduleEntity toEntity(Schedule schedule) {
        return new ScheduleEntity(
                schedule.id,
                SpecialLecture.toEntity(schedule.specialLecture),
                schedule.capacityCount,
                schedule.enrollCount,
                schedule.specialLectureDate
        );
    }
}
