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
    private int capacity_count;

    //신청 인원
    @Column(nullable = false)
    private int enroll_count;

    //특강 날짜
    @Column(nullable = false)
    private LocalDate speLecDate;

    public Schedule(Long id, SpecialLecture specialLecture, int capacity_count, int enroll_count, LocalDate speLecDate) {
        this.id = id;
        this.specialLecture = specialLecture;
        this.capacity_count = capacity_count;
        this.enroll_count = enroll_count;
        this.speLecDate = speLecDate;
    }
}
