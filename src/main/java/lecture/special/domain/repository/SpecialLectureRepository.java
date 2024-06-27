package lecture.special.domain.repository;

import jakarta.persistence.LockModeType;
import lecture.special.infra.entity.lecture.SpecialLecture;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface SpecialLectureRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SpecialLecture> findBySpeLecName(String speLecName);

    List<SpecialLecture> findAll();
}
