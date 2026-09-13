package org.soulmin.ecommerce.payment;

import org.soulmin.ecommerce.customer.CustomerResponse;
import org.soulmin.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderReference,
    CustomerResponse customer
) {
}
