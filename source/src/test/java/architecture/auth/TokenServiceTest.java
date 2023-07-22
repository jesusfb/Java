package architecture.auth;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import architecture.Data;

@SpringBootTest
class TokenServiceTest {
    @Autowired
    TokenService tokenService;

    @Test
    void verifyNullTokenShouldReturnFalse() {
        final var result = tokenService.verify(null);

        assertFalse(result);
    }

    @Test
    void verifyEmptyTokenShouldReturnFalse() {
        final var result = tokenService.verify("");

        assertFalse(result);
    }

    @Test
    void verifyInvalidTokenShouldReturnFalse() {
        final var result = tokenService.verify(UUID.randomUUID().toString());

        assertFalse(result);
    }

    @Test
    void verifyValidTokenShouldReturnTrue() {
        final var token = tokenService.create(Data.user);

        final var result = tokenService.verify(token);

        assertTrue(result);
    }
}
