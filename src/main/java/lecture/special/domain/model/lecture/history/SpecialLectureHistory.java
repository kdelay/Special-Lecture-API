package lecture.special.domain.model.lecture.history;

import jakarta.persistence.*;
import lecture.special.domain.model.lecture.SpecialLecture;
import lecture.special.domain.model.user.User;
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
    private User user;

    @ManyToOne
    private SpecialLecture specialLecture;

    //생성 날짜
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //수정 날짜
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public SpecialLectureHistory(User user, SpecialLecture specialLecture) {
        this.user = user;
        this.specialLecture = specialLecture;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
