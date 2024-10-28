package ozdemir0ozdemir.recipeshare.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.recipeshare.request.LoginRequest;
import ozdemir0ozdemir.recipeshare.request.RegisterRequest;
import ozdemir0ozdemir.recipeshare.response.Response;
import ozdemir0ozdemir.recipeshare.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService service;

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@RequestBody LoginRequest request) {
        log.info("login requested for email: {}", request.email());

        return service.login(request)
                .map(Response::forSucceededLogin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Response.forFailedLogin())
                );
    }

    @PostMapping
    ResponseEntity<Response<Void>> register(@RequestBody RegisterRequest request) {
        log.info("User registration requested for email: {}", request.email());

        Optional<Long> userID = service.createUser(request);

        if(userID.isEmpty()){
            log.info("User registration failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.forFailedRegister());
        }

        log.info("User registration succeeded for email: {}", request.email());
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/users/{userId}")
                        .buildAndExpand(userID.get().toString())
                        .toUri()
                )
                .body(Response.forSucceededRegister());
    }
}
