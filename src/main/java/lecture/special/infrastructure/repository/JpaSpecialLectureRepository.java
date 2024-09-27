package lecture.special.infrastructure.repository;

import jakarta.persistence.LockModeType;
import lecture.special.infrastructure.db.SpecialLectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface JpaSpecialLectureRepository extends JpaRepository<SpecialLectureEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SpecialLectureEntity> findLockBySpecialLectureName(String specialLectureName);
}
