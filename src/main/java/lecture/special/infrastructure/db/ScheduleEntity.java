package lecture.special.infrastructure.db;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule")
public class ScheduleEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //특강 id
    @ManyToOne
    @JoinColumn(name = "special_lecture_id", nullable = false)
    private SpecialLectureEntity specialLectureEntity;

    //수용 인원
    @Column(nullable = false)
    private int capacityCount;

    //신청 인원
    @Column(nullable = false)
    private int enrollCount;

    //특강 날짜
    @Column(nullable = false)
    private LocalDate specialLectureDate;

    public ScheduleEntity(Long id, SpecialLectureEntity specialLectureEntity, int capacityCount, int enrollCount, LocalDate specialLectureDate) {
        this.id = id;
        this.specialLectureEntity = specialLectureEntity;
        this.capacityCount = capacityCount;
        this.enrollCount = enrollCount;
        this.specialLectureDate = specialLectureDate;
    }
}
