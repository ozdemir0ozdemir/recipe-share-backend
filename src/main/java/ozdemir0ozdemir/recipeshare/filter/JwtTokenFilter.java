package ozdemir0ozdemir.recipeshare.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ozdemir0ozdemir.recipeshare.model.JwtAuthenticationToken;
import ozdemir0ozdemir.recipeshare.service.JwtService;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtService jwtService;
    private final AuthenticationManager manager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {


        String jwt = this.getJwt(request);
        String email = jwtService.getEmailFromJwtToken(jwt);

        if (jwt.isBlank() || email.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = JwtAuthenticationToken.unauthenticated(email);
        try {
            SecurityContextHolder.getContext()
                    .setAuthentication(manager.authenticate(authentication));
        } catch (Exception ex) {
            log.trace("Invalid JWT Token entered. Filter did not process");
        }


        chain.doFilter(request, response);

    }

    private String getJwt(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.trace("Auth Header Check: {}", authorizationHeader);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.trace("There is no Bearer Token in the authorization header. Jwt filter will not work");
            return "";
        }

        return authorizationHeader.substring("Bearer ".length());
    }
}
