package lecture.special.domain;

import lecture.special.infrastructure.db.SpecialLectureEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class SpecialLecture {
    private Long id;
    private String specialLectureName;
    private List<Schedule> schedules;

    public SpecialLecture(Long id, String specialLectureName) {
        this.id = id;
        this.specialLectureName = specialLectureName;
    }

    public SpecialLecture(Long id, String specialLectureName, List<Schedule> schedules) {
        this.id = id;
        this.specialLectureName = specialLectureName;
        this.schedules = schedules;
    }

    public static SpecialLecture toDomain(SpecialLectureEntity entity) {
        return new SpecialLecture(
                entity.getId(),
                entity.getSpecialLectureName(),
                entity.getSchedules().stream()
                        .map(Schedule::toDomain).toList()
        );
    }

    public static SpecialLectureEntity toEntity(SpecialLecture specialLecture) {
        return new SpecialLectureEntity(
                specialLecture.id,
                specialLecture.specialLectureName,
                specialLecture.schedules.stream()
                        .map(Schedule::toEntity).toList()
        );
    }
}