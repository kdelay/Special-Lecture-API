package lecture.special.domain.repository;

import jakarta.persistence.LockModeType;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.mapper.dto.SpecialLectureWithScheduleDTO;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpecialLectureRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SpecialLecture> findBySpeLecName(String speLecName);

    List<SpecialLecture> findAll();

    @Query("SELECT new lecture.special.infra.entity.mapper.dto.SpecialLectureWithScheduleDTO(" +
            "sl.id, sl.speLecName, s.id, s.capacityCount, s.enrollCount, s.speLecDate) " +
            "FROM SpecialLecture sl JOIN sl.schedules s")
    List<SpecialLectureWithScheduleDTO> findSpecialLectureWithSchedules();
}




