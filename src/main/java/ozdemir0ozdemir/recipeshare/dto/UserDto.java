package ozdemir0ozdemir.recipeshare.dto;

import ozdemir0ozdemir.recipeshare.model.User;

public record UserDto(String email, String fullName) {

    public static UserDto from(User user) {
        return new UserDto(user.getEmail(), user.getFullName());
    }
}
