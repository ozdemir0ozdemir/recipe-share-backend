package ozdemir0ozdemir.recipeshare.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.recipeshare.dto.UserDto;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.repository.UserRepository;
import ozdemir0ozdemir.recipeshare.request.LoginRequest;
import ozdemir0ozdemir.recipeshare.request.RegisterRequest;
import ozdemir0ozdemir.recipeshare.request.UpdateUserRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Page<UserDto> findAllUsers(int page, int pageSize) {
        log.info("Find All Users invoked for page: {}, pageSize: {}", page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.repository.findAll(pageable)
                .map(UserDto::from);
    }

    public Optional<Long> createUser(RegisterRequest request) {

        Optional<User> existingUser = findByEmail(request.email());
        if(existingUser.isPresent()){
            log.info("User already registered for this email: {}", request.email());
            return Optional.empty();
        }

        User tempUser = User.builder()
                .email(request.email())
                .fullName(request.fullName())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User createdUser = this.repository.save(tempUser);

        if(createdUser.getId() == null){
            log.info("Database error registering user with email: {}", request.email());
            return Optional.empty();
        }

        return Optional.of(createdUser.getId());
    }

    public Optional<User> findUserById(Long userId) {
        return this.repository.findById(userId);
    }

    public Optional<User> updateUser(UpdateUserRequest request, Long userId) {

        Optional<User> optionalUser = findUserById(userId);
        if(optionalUser.isEmpty()){
            log.info("User not found for user id: {}", userId);
            return Optional.empty();
        }

        User oldUser = optionalUser.get();
        oldUser.setFullName(request.fullName());
        oldUser.setPassword(passwordEncoder.encode(request.password()));

        return Optional.of(this.repository.save(oldUser));
    }

    public void deleteUserById(Long userId) {

        log.info("Delete User By User Id, invoked for user id: {}", userId);
        this.repository.deleteById(userId);
    }

    public Optional<User> findByEmail(String email) {

        log.info("Find By Email, invoked for email: {}", email);
        return this.repository.findByEmail(email);
    }

    public Optional<String> login(LoginRequest request) {

        Optional<User> user = findByEmail(request.email());
        if(user.isEmpty()){
            log.info("user not found for email: {}", request.email());
            return Optional.empty();
        }

        boolean isPasswordCorrect = passwordEncoder.matches(request.password(), user.get().getPassword());
        if (!isPasswordCorrect) {
            log.info("bad credentials entered for email: {}", request.email());
            return Optional.empty();
        }

        // FIXME: Should keep users jwt tokens?
        String jwt = jwtService.generateToken(user.get().getEmail());

        log.info("user found and jwt generated for email: {}", request.email());
        return Optional.of(jwt);
    }

}
