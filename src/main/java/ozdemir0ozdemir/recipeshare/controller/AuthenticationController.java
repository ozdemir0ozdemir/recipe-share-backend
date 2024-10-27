package ozdemir0ozdemir.recipeshare.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.request.CreateUserRequest;
import ozdemir0ozdemir.recipeshare.request.LoginRequest;
import ozdemir0ozdemir.recipeshare.response.LoginResponse;
import ozdemir0ozdemir.recipeshare.response.UserCreatedResponse;
import ozdemir0ozdemir.recipeshare.service.JwtService;
import ozdemir0ozdemir.recipeshare.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService service;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        log.info("login request: {}", request);
        User user = service.findByEmail(request.email(), request.password());

        // FIXME: AuthenticationController: Keep users jwts
        String jwt = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponse(jwt, "Successfully login"));
    }

    @PostMapping
    ResponseEntity<UserCreatedResponse> createUser(@RequestBody CreateUserRequest request) {

        service.findByEmail(request.email())
                .ifPresent(_ -> { throw new RuntimeException("User already been saved"); });

        User savedUser = service.createUser(request);
        String jwt = jwtService.generateToken(savedUser.getEmail());

        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path(savedUser.getId().toString()).build().toUri())
                .body(new UserCreatedResponse(jwt, "User created successfully"));
    }
}
