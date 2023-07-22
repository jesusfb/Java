package architecture.auth;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import architecture.Data;
import architecture.user.UserRepository;
import static architecture.Extensions.post;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenService tokenService;

    @MockBean
    UserRepository userRepository;

    @Test
    void authNullShouldReturnBadRequest() throws Exception {
        final var request = new AuthRequest(null, null);

        post(mockMvc, "/auth", request).andExpect(status().isBadRequest());
    }

    @Test
    void authEmptyShouldReturnBadRequest() throws Exception {
        final var request = new AuthRequest("", "");

        post(mockMvc, "/auth", request).andExpect(status().isBadRequest());
    }

    @Test
    void authInvalidUsernameShouldReturnBadRequest() throws Exception {
        final var request = new AuthRequest(UUID.randomUUID().toString(), Data.password);

        post(mockMvc, "/auth", request).andExpect(status().isBadRequest());
    }

    @Test
    void authInvalidPasswordShouldReturnBadRequest() throws Exception {
        when(userRepository.findByUsername(Data.username)).thenReturn(Optional.of(Data.user));

        final var request = new AuthRequest(Data.username, UUID.randomUUID().toString());

        post(mockMvc, "/auth", request).andExpect(status().isBadRequest());
    }

    @Test
    void authShouldReturnOk() throws Exception {
        when(userRepository.findByUsername(Data.username)).thenReturn(Optional.of(Data.user));

        final var request = new AuthRequest(Data.username, Data.password);

        post(mockMvc, "/auth", request).andExpect(status().isOk());
    }
}