package lecture.special.infrastructure.repository;

import lecture.special.domain.User;
import lecture.special.domain.interfaces.UserRepository;
import lecture.special.infrastructure.db.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(UserEntity userEntity) {
        jpaUserRepository.save(userEntity);
    }

    @Override
    public User findById(Long id) {
        return User.toDomain(jpaUserRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("사용자가 없습니다."))
        );
    }
}
