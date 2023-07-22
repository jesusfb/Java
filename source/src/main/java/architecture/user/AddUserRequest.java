package architecture.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AddUserRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank String username,
    @NotBlank String password) {
}
