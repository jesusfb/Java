package architecture;

import com.google.gson.Gson;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import architecture.auth.TokenService;

public final class Extensions {
    private Extensions() { }

    private static ResultActions method(MockMvc mockMvc, HttpMethod method, String url, Object request, TokenService tokenService) throws Exception {
        final var builder = request(method, url);
        
        if (tokenService != null) builder.header("Authorization", "Bearer " + tokenService.create(Data.user));
        
        if (request != null)  builder.contentType(APPLICATION_JSON).content(new Gson().toJson(request));
        
        return mockMvc.perform(builder);
    }

    public static ResultActions delete(MockMvc mockMvc, String url) throws Exception {
        return method(mockMvc, HttpMethod.DELETE, url, null, null);
    }

    public static ResultActions delete(MockMvc mockMvc, TokenService tokenService, String url) throws Exception {
        return method(mockMvc, HttpMethod.DELETE, url, null, tokenService);
    }

    public static ResultActions get(MockMvc mockMvc, String url) throws Exception {
        return method(mockMvc, HttpMethod.GET, url, null, null);
    }

    public static ResultActions get(MockMvc mockMvc, TokenService tokenService, String url) throws Exception {
        return method(mockMvc, HttpMethod.GET, url, null, tokenService);
    }

    public static ResultActions post(MockMvc mockMvc, String url, Object request) throws Exception {
        return method(mockMvc, HttpMethod.POST, url, request, null);
    }

    public static ResultActions post(MockMvc mockMvc, TokenService tokenService, String url, Object request) throws Exception {
        return method(mockMvc, HttpMethod.POST, url, request, tokenService);
    }

    public static ResultActions put(MockMvc mockMvc, String url, Object request) throws Exception {
        return method(mockMvc, HttpMethod.PUT, url, request, null);
    }

    public static ResultActions put(MockMvc mockMvc, TokenService tokenService, String url, Object request) throws Exception {
        return method(mockMvc, HttpMethod.PUT, url, request, tokenService);
    }

    public static ResultActions patch(MockMvc mockMvc, String url, Object request) throws Exception {
        return method(mockMvc, HttpMethod.PATCH, url, request, null);
    }

    public static ResultActions patch(MockMvc mockMvc, TokenService tokenService, String url, Object request) throws Exception {
        return method(mockMvc, HttpMethod.PATCH, url, request, tokenService);
    }
}