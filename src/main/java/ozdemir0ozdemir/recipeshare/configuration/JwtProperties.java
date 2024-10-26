package ozdemir0ozdemir.recipeshare.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter(value = AccessLevel.PUBLIC)
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private String authorizationHeader;
    private String authorizationTypeHeader;
    private String emailClaim;
    private String issuer;
}
