package lecture.special.infra.impl;

import lecture.special.domain.repository.ScheduleRepository;
import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.jpa.JpaScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JpaScheduleRepository jpaRepository;

    @Override
    public Optional<Schedule> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Schedule> findBySpecialLectureAndSpeLecDate(SpecialLecture specialLecture, LocalDate speLecDate) {
        return jpaRepository.findBySpecialLectureAndSpeLecDate(specialLecture, speLecDate);
    }
}
