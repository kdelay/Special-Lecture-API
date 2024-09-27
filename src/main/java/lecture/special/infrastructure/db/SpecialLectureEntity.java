package lecture.special.infrastructure.db;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "special_lecture")
public class SpecialLectureEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //특강 명
    @Column(nullable = false, length = 30)
    private String specialLectureName;

    @OneToMany(mappedBy = "specialLecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntity> schedules = new ArrayList<>();

    public SpecialLectureEntity(Long id, String specialLectureName) {
        this.id = id;
        this.specialLectureName = specialLectureName;
    }

    public SpecialLectureEntity(Long id, String specialLectureName, List<ScheduleEntity> schedules) {
        this.id = id;
        this.specialLectureName = specialLectureName;
        this.schedules = schedules;
    }
}
