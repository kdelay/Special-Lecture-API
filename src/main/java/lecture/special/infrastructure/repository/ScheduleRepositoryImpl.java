package lecture.special.infrastructure.repository;

import lecture.special.domain.Schedule;
import lecture.special.domain.SpecialLecture;
import lecture.special.domain.interfaces.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JpaScheduleRepository jpaRepository;

    @Override
    public Schedule findById(Long id) {
        return Schedule.toDomain(
                jpaRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("해당하는 날짜가 없습니다."))
        );
    }

    @Override
    public Schedule findSpecialLectureWithSchedules(SpecialLecture specialLecture, LocalDate SpecialLectureName) {
        return Schedule.toDomain(
                jpaRepository.findBySpecialLectureEntityAndSpecialLectureDate(SpecialLecture.toEntity(specialLecture), SpecialLectureName)
                        .orElseThrow(() -> new NoSuchElementException("특강과 날짜에 해당하는 데이터가 없습니다."))
        );
    }
}
