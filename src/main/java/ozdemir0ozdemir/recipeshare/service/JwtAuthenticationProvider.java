package ozdemir0ozdemir.recipeshare.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.recipeshare.model.JwtAuthenticationToken;
import ozdemir0ozdemir.recipeshare.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = (JwtAuthenticationToken) authentication;
        log.trace("JWT Authentication for email: {}", auth.getName());

        User user = userService.findByEmail(auth.getName())
                .orElseThrow(() -> new AuthenticationException("User not found") {
                });
        // FIXME: Make users has authorities and permissions and authenticate the user here
        return JwtAuthenticationToken.authenticated(
                user.getEmail(),
                user,
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.equals(authentication);
    }
}
