package architecture.user;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import architecture.shared.MessageService;
import architecture.shared.Result;

@Service
@RequiredArgsConstructor
public final class UserService {
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static UserModel convert(User entity) {
        final var model = new UserModel();

        BeanUtils.copyProperties(entity, model);

        return model;
    }

    public Result<List<UserModel>> list() {
        final var entities = userRepository.findAll();

        if (entities.isEmpty()) return Result.success();

        final var models = entities.stream().map(UserService::convert).toList();

        return Result.success(models);
    }

    public Result<UserModel> get(UUID id) {
        final var entity = userRepository.findById(id);

        if (entity.isEmpty()) return Result.success();

        final var model = convert(entity.get());

        return Result.success(model);
    }

    public Result<UUID> add(AddUserRequest request) {
        final var exists = userRepository.existsByEmailOrUsername(request.email(), request.username());

        if (exists) return Result.error(messageService.get("error"));

        final var entity = new User(UUID.randomUUID(), request.name(), request.email(), request.username(), passwordEncoder.encode(request.password()));

        final var result = userRepository.insert(entity);

        return Result.success(result.getId());
    }

    public Result<Void> update(UpdateUserRequest request) {
        final var entity = userRepository.findById(request.id());

        if (entity.isEmpty()) return Result.success();

        final var exists = userRepository.existsByEmailOrUsername(request.id(), request.email(), request.username());

        if (exists) return Result.error(messageService.get("error"));

        BeanUtils.copyProperties(request, entity.get());

        entity.get().setPassword(passwordEncoder.encode(entity.get().getPassword()));

        userRepository.save(entity.get());

        return Result.success();
    }

    public Result<Void> delete(UUID id) {
        userRepository.deleteById(id);

        return Result.success();
    }
}
