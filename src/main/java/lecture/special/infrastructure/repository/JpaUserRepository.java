package lecture.special.infrastructure.repository;

import lecture.special.infrastructure.db.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
}