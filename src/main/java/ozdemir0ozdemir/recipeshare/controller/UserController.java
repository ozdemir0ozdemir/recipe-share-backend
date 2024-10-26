package ozdemir0ozdemir.recipeshare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.response.MeResponse;
import ozdemir0ozdemir.recipeshare.service.JwtService;
import ozdemir0ozdemir.recipeshare.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserService service;

    @GetMapping
    ResponseEntity<Set<User>> getAllUsers() {
        // FIXME: Pagination and filter
        Set<User> users = service.findAllUsers(0, 5).toSet();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    ResponseEntity<MeResponse> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return service.findByEmail(authentication.getName())
                .map(user -> new MeResponse(user.getEmail(), user.getFullName()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{userId}")
    ResponseEntity<User> findUserById(@PathVariable Long userId) {

        return ResponseEntity.ok(service.findUserById(userId));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<?> deleteUserById(@PathVariable Long userId) {

        service.deleteUserById(userId);

        return ResponseEntity.noContent().build();
    }

}
