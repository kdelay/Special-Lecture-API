package lecture.special.infra.repository.user;

import lecture.special.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {
}