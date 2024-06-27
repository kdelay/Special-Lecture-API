package lecture.special.infra.entity.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //특강 id
    @ManyToOne
    @JoinColumn(name = "special_lecture_id", nullable = false)
    private SpecialLecture specialLecture;

    //수용 인원
    @Column(nullable = false)
    private int capacityCount;

    //신청 인원
    @Column(nullable = false)
    private int enrollCount;

    //특강 날짜
    @Column(nullable = false)
    private LocalDate speLecDate;

    public Schedule(Long id, SpecialLecture specialLecture, int capacityCount, int enrollCount, LocalDate speLecDate) {
        this.id = id;
        this.specialLecture = specialLecture;
        this.capacityCount = capacityCount;
        this.enrollCount = enrollCount;
        this.speLecDate = speLecDate;
    }

    public void plusEnrollCount() {
        this.enrollCount++;
    }
}
