package lecture.special.infra.repository.user;

import lecture.special.domain.model.user.User;
import lecture.special.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(User user) {
        jpaUserRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return jpaUserRepository.findById(id).orElse(null);
    }
}
