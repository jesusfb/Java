package architecture.product;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import architecture.shared.Result;

@Service
@RequiredArgsConstructor
public final class ProductService {
    private final ProductRepository productRepository;

    private static ProductModel convert(Product entity) {
        final var model = new ProductModel();

        BeanUtils.copyProperties(entity, model);

        return model;
    }

    private <T> Result<Void> update(UUID id, T request) {
        final var entity = productRepository.findById(id);

        if (entity.isEmpty())
            return Result.success();

        BeanUtils.copyProperties(request, entity.get());

        productRepository.save(entity.get());

        return Result.success();
    }

    public Result<List<ProductModel>> list() {
        final var entities = productRepository.findAll();

        if (entities.isEmpty())
            return Result.success();

        final var models = entities.stream().map(ProductService::convert).toList();

        return Result.success(models);
    }

    public Result<ProductModel> get(UUID id) {
        final var entity = productRepository.findById(id);

        if (entity.isEmpty())
            return Result.success();

        final var model = convert(entity.get());

        return Result.success(model);
    }

    public Result<Void> delete(UUID id) {
        productRepository.deleteById(id);

        return Result.success();
    }

    public Result<UUID> add(AddProductRequest request) {
        final var entity = new Product(UUID.randomUUID(), request.description(), request.price());

        final var result = productRepository.insert(entity);

        return Result.success(result.getId());
    }

    public Result<Void> update(UpdateProductRequest request) {
        return update(request.id(), request);
    }

    public Result<Void> update(UpdatePriceProductRequest request) {
        return update(request.id(), request);
    }
}
