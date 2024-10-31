package ozdemir0ozdemir.recipeshare.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.recipeshare.dto.UserDto;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.request.UpdateUserRequest;
import ozdemir0ozdemir.recipeshare.response.PagedResponse;
import ozdemir0ozdemir.recipeshare.response.Response;
import ozdemir0ozdemir.recipeshare.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService service;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')") // FIXME: Admin endpoint
    ResponseEntity<PagedResponse<Set<UserDto>>> getAllUsers(@RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) {

        // Page number starts 1 for request but 0 for database
        // TODO: Make global sanitize for page properties
        int securedPage = Math.max(0, page - 1);
        int securedPageSize = Math.max(5, pageSize);
        log.info("Get All Users requested with page: {}, and pageSize: {}", securedPage, securedPageSize);

        Page<UserDto> userDtosPage =
                service.findAllUsers(securedPage, securedPageSize);

        if (userDtosPage.isEmpty()) {
            log.info("No users found in the database");
        }

        PagedResponse<Set<UserDto>> response = PagedResponse.of(
                userDtosPage.toSet(),
                userDtosPage,
                "Users retrieved with pagination");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<Response<UserDto>> me() {
        log.info("Me endpoint requested");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return service.findByEmail(authentication.getName())
                .map(UserDto::from)
                .map(Response::forUserResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.forUnauthorizedRequests()));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER')") // FIXME: Admin endpoint
    ResponseEntity<Response<UserDto>> findUserById(@PathVariable Long userId) {
        log.info("Find User By Id, requested for user id: {}", userId);

        return service.findUserById(userId)
                .map(UserDto::from)
                .map(Response::forUserResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.info("User not found with user id: {}", userId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Response.ofFailed(null, "User not found"));
                });
    }

    @PatchMapping("/{userId}") // FIXME: The user would changed is me?
    ResponseEntity<Response<UserDto>> updateUser(@PathVariable Long userId,
                                                 @RequestBody UpdateUserRequest request) {

        log.info("Update user requested for user id: {}", userId);

        if(this.isUserDifferentToAuthenticatedUser(userId)){
            log.info("The user you want to change is different to authenticated user");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Response.forForbiddenRequests());
        }

        return this.service.updateUser(request, userId)
                .map(UserDto::from)
                .map(Response::forUserResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.info("User not found while trying to update the user with id: {}", userId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.forUserNotFound());
                });
    }

    @DeleteMapping("/{userId}") // FIXME: Admin endpoint
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        service.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    private boolean isUserDifferentToAuthenticatedUser(Long userId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean result = ((User)authentication.getPrincipal()).getId().equals(userId);

        return !result;
    }

}
