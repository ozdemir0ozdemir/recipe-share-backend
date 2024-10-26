package ozdemir0ozdemir.recipeshare.service;

import lombok.RequiredArgsConstructor;
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

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = (JwtAuthenticationToken) authentication;

        User user = userService.findByEmail(auth.getName())
                .orElseThrow(() -> new AuthenticationException("User not found") {
                });

        // FIXME: JwtAuthenticationProvider: Load users authorities
        return JwtAuthenticationToken.authenticated(user.getEmail(),
                List.of(new SimpleGrantedAuthority("USER")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.equals(authentication);
    }
}
