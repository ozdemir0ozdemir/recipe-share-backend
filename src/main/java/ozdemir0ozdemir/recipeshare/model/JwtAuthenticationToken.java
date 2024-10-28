package ozdemir0ozdemir.recipeshare.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

public class JwtAuthenticationToken implements Authentication {

    private boolean authenticated = false;
    private String email;
    private String password;
    private User user;
    private List<GrantedAuthority> authorities;

    private JwtAuthenticationToken(String email) {
        this.email = email;
        this.authorities = new ArrayList<>();
        this.setAuthenticated(false);
    }

    private JwtAuthenticationToken(
            String email,
            User authenticatedUser,
            List<GrantedAuthority> authorities) {
        this.email = email;
        this.user = authenticatedUser;
        this.authorities = authorities;
        this.setAuthenticated(true);
    }

    public static JwtAuthenticationToken unauthenticated(String email) {
        return new JwtAuthenticationToken(email);
    }

    public static JwtAuthenticationToken authenticated(String email,
                                                       User authenticatedUser,
                                                       List<GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(email, authenticatedUser, authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getCredentials() {
        return this.password;
    }

    @Override
    public String getDetails() {
        // TODO: may be it is a request id
        return "What should i return?";
    }

    @Override
    public User getPrincipal() {
        return this.user;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = true;
    }

    @Override
    public String getName() {
        return this.email;
    }


}
