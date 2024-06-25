package lecture.special.domain.model.user;

public interface UserRepository {

    void save(User user);

    User findById(Long id);
}
