package lecture.special.domain.service.business;

import lecture.special.domain.repository.UserRepository;
import lecture.special.infra.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class SpecialLectureDomain {

    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자가 없습니다."));
    }
}