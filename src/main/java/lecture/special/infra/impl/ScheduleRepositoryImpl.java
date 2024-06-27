package lecture.special.infra.impl;

import lecture.special.infra.entity.lecture.Schedule;
import lecture.special.domain.repository.ScheduleRepository;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.jpa.JpaScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JpaScheduleRepository jpaRepository;

    @Override
    public Schedule findById(Long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public Schedule findBySpecialLecture(SpecialLecture specialLecture) {
        return jpaRepository.findBySpecialLecture(specialLecture).orElse(null);
    }
}
