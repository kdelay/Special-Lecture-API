package lecture.special.infra.impl;

import lecture.special.infra.entity.lecture.SpecialLecture;
import lecture.special.domain.repository.SpecialLectureRepository;
import lecture.special.infra.jpa.JpaSpecialLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SpecialLectureRepositoryImpl implements SpecialLectureRepository {

    private final JpaSpecialLectureRepository jpaLectureRepository;

    @Override
    public SpecialLecture findBySpeLecName(String speLecName) {
        return jpaLectureRepository.findBySpeLecName(speLecName).orElse(null);
    }

    @Override
    public List<SpecialLecture> findAll() {
        return jpaLectureRepository.findAll();
    }
}
