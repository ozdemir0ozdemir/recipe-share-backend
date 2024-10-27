package ozdemir0ozdemir.recipeshare.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.response.MeResponse;
import ozdemir0ozdemir.recipeshare.service.UserService;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService service;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Set<User>> getAllUsers() {
        // FIXME: Pagination and filter
        // FIXME: authentication problem
        Set<User> users = service.findAllUsers(0, 5).toSet();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    ResponseEntity<MeResponse> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.ok(new MeResponse("Anonymous", "Anonymous"));
        }

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
