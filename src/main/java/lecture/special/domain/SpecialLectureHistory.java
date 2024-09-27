package lecture.special.domain;

import lecture.special.infrastructure.db.SpecialLectureHistoryEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SpecialLectureHistory {
    private Long id;
    private User user;
    private Schedule schedule;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SpecialLectureHistory(Long id, User user, Schedule schedule, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.schedule = schedule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public SpecialLectureHistory(User user, Schedule schedule) {
        this.user = user;
        this.schedule = schedule;
    }

    public static SpecialLectureHistory toDomain(SpecialLectureHistoryEntity entity) {
        return new SpecialLectureHistory(
                entity.getId(),
                User.toDomain(entity.getUserEntity()),
                Schedule.toDomain(entity.getScheduleEntity()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static SpecialLectureHistoryEntity toEntity(SpecialLectureHistory specialLectureHistory) {
        return new SpecialLectureHistoryEntity(
                specialLectureHistory.id,
                User.toEntity(specialLectureHistory.user),
                Schedule.toEntity(specialLectureHistory.schedule),
                specialLectureHistory.createdAt,
                specialLectureHistory.updatedAt
        );
    }
}
