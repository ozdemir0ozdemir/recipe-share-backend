package ozdemir0ozdemir.recipeshare.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response<T> {

    private T data;
    private String message;
    private ResponseStatus status;

    public enum ResponseStatus {
        SUCCEEDED,
        FAILED,
        UNAUTHORIZED
    }

    // Static factory methods for specific response types
    // Generic
    public static <T> Response<T> ofSucceeded(T data, String message){
        return new Response<>(data, message, ResponseStatus.SUCCEEDED);
    }

    public static <T> Response<T> ofFailed(T data, String message){
        return new Response<>(data, message, ResponseStatus.FAILED);
    }

    // Login
    public static <T> Response<T> forSucceededLogin(T data) {
        return new Response<>(data, "Successfully logged in", ResponseStatus.SUCCEEDED);
    }

    public static <T> Response<T> forFailedLogin() {
        return new Response<>(null, "Login failed", ResponseStatus.FAILED);
    }

    // Register
    public static <T> Response<T> forSucceededRegister() {
        return new Response<>(null, "User successfully registered", ResponseStatus.SUCCEEDED);
    }

    public static <T> Response<T> forFailedRegister() {
        return new Response<>(null, "User registration failed", ResponseStatus.FAILED);
    }

    // By Authentication
    public static <T> Response<T> forUserResponse(T data) {
        return new Response<>(data, "Your profile retrieved", ResponseStatus.SUCCEEDED);
    }

    public static <T> Response<T> forUnauthorizedRequests() {
        return new Response<>(null, "You must be authenticated to reach this endpoint", ResponseStatus.UNAUTHORIZED);
    }

    public static <T> Response<T> forForbiddenRequests() {
        return new Response<>(null, "You are forbidden", ResponseStatus.FAILED);
    }

    public static <T> Response<T> forUserNotFound() {
        return new Response<>(null, "User not found", ResponseStatus.FAILED);
    }

}
