package ozdemir0ozdemir.recipeshare.service;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.recipeshare.configuration.JwtProperties;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(Authentication auth) {
        return generateToken(auth.getName());
    }

    public String generateToken(String email) {

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + Duration.ofDays(1L).toMillis()))
                .setIssuer(jwtProperties.getIssuer())
                .claim(jwtProperties.getEmailClaim(), email)
                .signWith(getSecretKey())
                .compact();
    }

    // TODO: Error trace log
    public String getEmailFromJwtToken(String jwt) {
        return getClaimByName(jwt, jwtProperties.getEmailClaim());
    }

    private String getClaimByName(String jwt, String claimName) {
        try {
            return this.getParser().parseClaimsJws(jwt)
                    .getBody()
                    .get(claimName, String.class);
        }
        catch (Exception ex) {
            return "";
        }
    }

    private JwtParser getParser() {
        return Jwts.parserBuilder()
                .requireIssuer(jwtProperties.getIssuer())
                .setSigningKey(getSecretKey())
                .build();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(this.jwtProperties.getSecret().getBytes());
    }


}
