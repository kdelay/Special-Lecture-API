package lecture.special.infrastructure.db;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "special_lecture_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "schedule_id"}))
public class SpecialLectureHistoryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저 id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    //특강 일정 id
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity scheduleEntity;

    //생성 날짜
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //수정 날짜
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public SpecialLectureHistoryEntity(UserEntity userEntity, ScheduleEntity scheduleEntity) {
        this.userEntity = userEntity;
        this.scheduleEntity = scheduleEntity;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public SpecialLectureHistoryEntity(Long id, UserEntity userEntity, ScheduleEntity scheduleEntity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userEntity = userEntity;
        this.scheduleEntity = scheduleEntity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
