package lecture.special.infra.entity.lecture;

import jakarta.persistence.*;
import lecture.special.infra.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "special_lecture_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "schedule_id"}))
public class SpecialLectureHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저 id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    //생성 날짜
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //수정 날짜
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public SpecialLectureHistory(User user, Schedule schedule) {
        this.user = user;
        this.schedule = schedule;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
