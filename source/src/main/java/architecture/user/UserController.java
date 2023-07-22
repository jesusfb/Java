package architecture.user;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public final class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> list() {
        return userService.list().response();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> get(@PathVariable UUID id) {
        return userService.get(id).response();
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid AddUserRequest request) {
        return userService.add(request).response();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest request) {
        return userService.update(request).response();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        return userService.delete(id).response();
    }
}
