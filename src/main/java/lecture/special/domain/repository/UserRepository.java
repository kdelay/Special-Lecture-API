package lecture.special.domain.repository;

import lecture.special.infra.entity.user.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(Long id);
}
