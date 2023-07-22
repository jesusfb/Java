package architecture.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import architecture.shared.MessageService;
import architecture.shared.Result;
import architecture.user.UserRepository;

@Service
@RequiredArgsConstructor
public final class AuthService {
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public Result<String> auth(AuthRequest request) {
        final var user = userRepository.findByUsername(request.username());

        if (user.isEmpty())
            return Result.error(messageService.get("invalidUsername"));

        if (!passwordEncoder.matches(request.password(), user.get().getPassword()))
            return Result.error(messageService.get("invalidPassword"));

        final var token = tokenService.create(user.get());

        return Result.success(token);
    }
}
