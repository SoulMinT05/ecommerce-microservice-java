package org.soulmin.ecommerce.kafka;

import org.soulmin.ecommerce.customer.CustomerResponse;
import org.soulmin.ecommerce.order.PaymentMethod;
import org.soulmin.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse>products
) {
}
