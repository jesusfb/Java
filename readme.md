# JAVA

API using Spring Boot, MongoDB, JWT Authentication and Authorization, Internationalization, Swagger, Result Pattern, Folder-by-Feature Structure.

## TECHNOLOGIES

* [Java](https://dev.java)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [MongoDB](https://www.mongodb.com/docs/manual)

## RUN

<details>
<summary>Visual Studio Code</summary>

#### Prerequisites

* [Java JDK](https://www.oracle.com/java/technologies/downloads)
* [Visual Studio Code](https://code.visualstudio.com)
* [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
* [MongoDB](https://www.mongodb.com/try/download/community)

#### Steps

1. Open **source** directory in **Visual Studio Code**.
2. Select **Application.java** class.
3. Press **F5**.
4. Open <http://localhost:8080>.

</details>

<details>
<summary>IntelliJ IDEA</summary>

#### Prerequisites

* [Java JDK](https://www.oracle.com/java/technologies/downloads)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/download)
* [MongoDB](https://www.mongodb.com/try/download/community)

#### Steps

1. Open **source** directory in **IntelliJ IDEA**.
2. Select **Application.java** class.
3. Click **Run** or **Debug**.
4. Open <http://localhost:8080>.

</details>

<details>
<summary>Docker</summary>

#### Prerequisites

* [Docker](https://www.docker.com/get-started)

#### Steps

1. Execute **docker compose -p java up --build -d** in **docker** directory.
2. Open <http://localhost:8080>.

</details>

## CODE

```java
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public final class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Object> list() {
        return productService.list().response();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> get(@PathVariable UUID id) {
        return productService.get(id).response();
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid AddProductRequest request) {
        return productService.add(request).response();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid UpdateProductRequest request) {
        return productService.update(request).response();
    }

    @PatchMapping("{id}/price/{price}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @PathVariable BigDecimal price) {
        return productService.update(new UpdatePriceProductRequest(id, price)).response();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        return productService.delete(id).response();
    }
}
```

```java
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
```

```java
public interface ProductRepository extends MongoRepository<Product, UUID> { }
```
