package lecture.special.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialLectureHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users users;

    @ManyToOne
    private SpecialLecture specialLecture;

    //생성 날짜
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //수정 날짜
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
