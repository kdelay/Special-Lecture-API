package lecture.special.infra.impl;

import lecture.special.infra.entity.user.User;
import lecture.special.domain.repository.UserRepository;
import lecture.special.infra.jpa.JpaUserRepository;
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
