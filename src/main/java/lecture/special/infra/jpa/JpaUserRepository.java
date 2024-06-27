package lecture.special.infra.jpa;

import lecture.special.infra.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {
}