package architecture.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth")
@Data
public class AuthConfiguration {
    private String issuer;
    private String audience;
    private String secret;
}
