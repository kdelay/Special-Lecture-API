package lecture.special.infra.entity.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialLecture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //특강 명
    @Column(nullable = false, length = 30)
    private String speLecName;

    @OneToMany(mappedBy = "specialLecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    public SpecialLecture(Long id, String speLecName) {
        this.id = id;
        this.speLecName = speLecName;
    }
}
