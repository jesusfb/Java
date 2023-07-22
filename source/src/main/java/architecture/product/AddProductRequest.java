package architecture.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record AddProductRequest(
    @NotBlank String description,
    @Min(0) BigDecimal price) {
}
