package ozdemir0ozdemir.recipeshare.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private List<String> authenticatedServletPaths;
    private List<String> allowedServletPaths;
}
