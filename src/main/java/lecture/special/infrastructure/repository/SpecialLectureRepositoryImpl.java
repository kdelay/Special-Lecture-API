package lecture.special.infrastructure.repository;

import lecture.special.domain.SpecialLecture;
import lecture.special.domain.interfaces.SpecialLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class SpecialLectureRepositoryImpl implements SpecialLectureRepository {

    private final JpaSpecialLectureRepository jpaLectureRepository;

    @Override
    public SpecialLecture findBySpecialLectureName(String specialLectureName) {
        return SpecialLecture.toDomain(
                jpaLectureRepository.findLockBySpecialLectureName(specialLectureName)
                        .orElseThrow(() -> new NoSuchElementException("해당하는 특강이 없습니다."))
        );
    }

    @Override
    public List<SpecialLecture> findAll() {
        return jpaLectureRepository.findAll().stream()
                .map(SpecialLecture::toDomain)
                .toList();
    }
}
