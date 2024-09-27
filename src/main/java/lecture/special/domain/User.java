package lecture.special.domain;

import lecture.special.infrastructure.db.UserEntity;
import lombok.Getter;

@Getter
public class User {
    private Long userId;

    public User(Long userId) {
        this.userId = userId;
    }

    public static User toDomain(UserEntity userEntity) {
        return new User(userEntity.getUserId());
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(user.userId);
    }
}