package lecture.special.domain.interfaces;

import lecture.special.domain.User;
import lecture.special.infrastructure.db.UserEntity;

public interface UserRepository {

    void save(UserEntity userEntity);

    User findById(Long id);
}
