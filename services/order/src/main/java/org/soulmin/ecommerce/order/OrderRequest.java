package org.soulmin.ecommerce.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.soulmin.ecommerce.product.PurchaseRequest;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
    Integer id,
    String reference,
    @Positive(message = "Order amount should be positive")
    BigDecimal amount,
    @NotNull(message = "Payment method should be provided")
    PaymentMethod paymentMethod,
    @NotNull(message = "Customer ID should be provided")
    @NotEmpty(message = "Customer ID should be provided")
    @NotBlank(message = "Customer ID should be provided")
    String customerId,
    @NotEmpty(message = "You should purchase at least one product")
    List<PurchaseRequest> products
) {
}
