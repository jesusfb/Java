package architecture.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import architecture.user.User;

@Service
@RequiredArgsConstructor
public final class TokenService {
    private final AuthConfiguration authConfiguration;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(authConfiguration.getSecret());
    }

    public String create(User user) {
        return JWT
            .create()
            .withJWTId(UUID.randomUUID().toString())
            .withNotBefore(new Date()).withIssuedAt(new Date())
            .withIssuer(authConfiguration.getIssuer())
            .withAudience(authConfiguration.getAudience())
            .withSubject(user.getId().toString())
            .withArrayClaim("authorities", user.getAuthorities())
            .sign(getAlgorithm());
    }

    public boolean verify(String token) {
        try {
            JWT
                .require(getAlgorithm())
                .withIssuer(authConfiguration.getIssuer())
                .withAudience(authConfiguration.getAudience())
                .build()
                .verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        return AuthorityUtils.createAuthorityList(JWT.decode(token).getClaim("authorities").asList(String.class));
    }
}
