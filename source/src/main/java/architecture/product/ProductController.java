package architecture.product;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
