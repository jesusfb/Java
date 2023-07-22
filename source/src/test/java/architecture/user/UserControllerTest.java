package architecture.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import architecture.Data;
import architecture.auth.TokenService;
import static architecture.Extensions.delete;
import static architecture.Extensions.get;
import static architecture.Extensions.post;
import static architecture.Extensions.put;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    TokenService tokenService;
    
    @MockBean
    UserRepository userRepository;
    
    @Test
    void listShouldReturnForbidden() throws Exception {
        get(mockMvc, "/users").andExpect(status().isForbidden());
    }
    
    @Test
    void listShouldReturnNotFound() throws Exception {
        get(mockMvc, tokenService, "/users").andExpect(status().isNotFound());
    }
    
    @Test
    void listShouldReturnOk() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(Data.user));
        
        get(mockMvc, tokenService, "/users").andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getShouldReturnForbidden() throws Exception {
        get(mockMvc, "/users/" + Data.id).andExpect(status().isForbidden());
    }
    
    @Test
    void getShouldReturnNotFound() throws Exception {
        get(mockMvc, tokenService, "/users/" + UUID.randomUUID()).andExpect(status().isNotFound());
    }
    
    @Test
    void getShouldReturnOk() throws Exception {
        when(userRepository.findById(Data.id)).thenReturn(Optional.of(Data.user));
        
        get(mockMvc, tokenService, "/users/" + Data.id).andExpect(status().isOk());
    }

    @Test
    void addShouldReturnForbidden() throws Exception {
        final var request = new AddUserRequest(Data.name, Data.email, Data.username, Data.password);

        post(mockMvc, "/users", request).andExpect(status().isForbidden());
    }
    
    @Test
    void addNullShouldReturnBadRequest() throws Exception {
        final var request = new AddUserRequest(null, null, null, null);

        post(mockMvc, tokenService, "/users", request).andExpect(status().isBadRequest());
    }
    
    @Test
    void addEmptyShouldReturnBadRequest() throws Exception {
        final var request = new AddUserRequest("", "", "", "");

        post(mockMvc, tokenService, "/users", request).andExpect(status().isBadRequest());
    }

    @Test
    void addExistentEmailOrUsernameShouldReturnBadRequest() throws Exception {
        when(userRepository.existsByEmailOrUsername(Data.email, Data.username)).thenReturn(true);
        
        final var request = new AddUserRequest(Data.name, Data.email, Data.username, Data.password);
        
        post(mockMvc, tokenService, "/users", request).andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturnOk() throws Exception {
        when(userRepository.insert((User)any())).thenReturn(Data.user);
        
        final var request = new AddUserRequest(Data.name, Data.email, Data.username, Data.password);
        
        post(mockMvc, tokenService, "/users", request).andExpect(status().isOk());
    }

    @Test
    void updateShouldReturnForbidden() throws Exception {
        final var request = new UpdateUserRequest(Data.id, Data.name, Data.email, Data.username, Data.password);

        put(mockMvc, "/users/" + Data.id, request).andExpect(status().isForbidden());
    }
    
    @Test
    void updateNullShouldReturnBadRequest() throws Exception {
        final var request = new UpdateUserRequest(null, null, null, null, null);

        put(mockMvc, tokenService, "/users/" + Data.id, request).andExpect(status().isBadRequest());
    }
    
    @Test
    void updateEmptyShouldReturnBadRequest() throws Exception {
        final var request = new UpdateUserRequest(Data.id, "", "", "", "");

        put(mockMvc, tokenService, "/users/" + Data.id, request).andExpect(status().isBadRequest());
    }

    @Test
    void updateNonExistentRequestShouldReturnNoContent() throws Exception {
        when(userRepository.findById(Data.id)).thenReturn(Optional.empty());
        
        final var request = new UpdateUserRequest(Data.id, Data.name, Data.email, Data.username, Data.password);

        put(mockMvc, tokenService, "/users/" + Data.id, request).andExpect(status().isNoContent());
    }

    @Test
    void updateExistentEmailOrUsernameShouldReturnBadRequest() throws Exception {
        when(userRepository.findById(Data.id)).thenReturn(Optional.of(Data.user));

        when(userRepository.existsByEmailOrUsername(Data.id, Data.email, Data.username)).thenReturn(true);
        
        final var request = new UpdateUserRequest(Data.id, Data.name, Data.email, Data.username, Data.password);

        put(mockMvc, tokenService, "/users/" + Data.id, request).andExpect(status().isBadRequest());
    }

    @Test
    void updateShouldReturnNoContent() throws Exception {
        when(userRepository.findById(Data.id)).thenReturn(Optional.of(Data.user));

        final var request = new UpdateUserRequest(Data.id, Data.name, Data.email, Data.username, Data.password);

        put(mockMvc, tokenService, "/users/" + Data.id, request).andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnForbidden() throws Exception {
        delete(mockMvc, "/users/" + Data.id).andExpect(status().isForbidden());
    }
    
    @Test
    void deleteNonExistentShouldReturnNoContent() throws Exception {
        delete(mockMvc, tokenService, "/users/" + UUID.randomUUID()).andExpect(status().isNoContent());
    }
    
    @Test
    void deleteExistentShouldReturnNoContent() throws Exception {
        delete(mockMvc, tokenService, "/users/" + Data.id).andExpect(status().isNoContent());
    }
}