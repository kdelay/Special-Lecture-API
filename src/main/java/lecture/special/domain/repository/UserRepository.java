package lecture.special.domain.repository;

import lecture.special.infra.entity.user.User;

public interface UserRepository {

    void save(User user);

    User findById(Long id);
}
