package ozdemir0ozdemir.recipeshare.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.repository.UserRepository;
import ozdemir0ozdemir.recipeshare.request.CreateUserRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findAllUsers(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(Math.max(0, pageNumber - 1), Math.max(5, pageSize));

        return this.repository.findAll(pageable);
    }

    public User createUser(CreateUserRequest request) {

        User createdUser = User.builder()
                .email(request.email())
                .fullName(request.fullName())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return this.repository.save(createdUser);
    }

    public User findUserById(Long userId) {

        return this.repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User updateUser(User request, Long userId) {

        User oldUser = findUserById(userId);
        oldUser.setFullName(request.getFullName());
        oldUser.setPassword(passwordEncoder.encode(request.getPassword()));

        return this.repository.save(oldUser);
    }

    public void deleteUserById(Long userId) {

        this.repository.deleteById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    public User findByEmail(String email, String password) {

        // FIXME: Custom Exception
        User user = this.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());

        if (!isPasswordCorrect) {
            throw new RuntimeException("Invalid user credentials");
        }

        return user;
    }

}
