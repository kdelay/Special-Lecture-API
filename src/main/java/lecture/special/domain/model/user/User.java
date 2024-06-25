package lecture.special.domain.model.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    //특강 신청 여부
    @Column(nullable = false)
    private boolean is_enrolled;

    public User(Long userId) {
        this.userId = userId;
        this.is_enrolled = false;
    }
}