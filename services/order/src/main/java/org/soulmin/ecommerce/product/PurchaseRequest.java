package org.soulmin.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseRequest(
    @NotNull(message = "Product ID should be provided")
    Integer productId,
    @Positive(message = "Quantity should be positive")
    double quantity
) {
}
