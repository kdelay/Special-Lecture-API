package lecture.special.infra.impl;

import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.infra.entity.mapper.dto.SpecialLectureWithScheduleDTO;
import lecture.special.infra.jpa.JpaSpecialLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpecialLectureRepositoryImpl implements SpecialLectureRepository {

    private final JpaSpecialLectureRepository jpaLectureRepository;

    @Override
    public Optional<SpecialLecture> findBySpeLecName(String speLecName) {
        return jpaLectureRepository.findBySpeLecName(speLecName);
    }

    @Override
    public List<SpecialLectureWithScheduleDTO> findSpecialLectureWithSchedules() {
        return jpaLectureRepository.findSpecialLectureWithSchedules();
    }
}
