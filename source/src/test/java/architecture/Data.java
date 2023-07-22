package architecture;

import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import architecture.user.User;

public final class Data {
    public static final UUID id = UUID.randomUUID();
    public static final String name = "Admin";
    public static final String email = "admin@mail.com";
    public static final String username = "admin";
    public static final String password = "123456";
    public static final User user = new User(id, name, email, username, new BCryptPasswordEncoder().encode(password));
}
