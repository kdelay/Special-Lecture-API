package lecture.special.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialLecture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //특강 명
    @Column(nullable = false, length = 30)
    private String speLecName;

    //수용 인원
    @Column(nullable = false)
    private int capacity;

    //특강 날짜
    @Column(nullable = false)
    private LocalDate speLecDate;
}
