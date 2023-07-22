package architecture.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateUserRequest(
    @NotNull UUID id,
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank String username,
    @NotBlank String password) {
}
