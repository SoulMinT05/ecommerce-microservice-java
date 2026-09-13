package org.soulmin.ecommerce.notification;

import org.soulmin.ecommerce.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationRequest(
            String orderReference,
            BigDecimal amount,
            PaymentMethod paymentMethod,
            String customerFirstName,
            String customerLastName,
            String customerEmail
) {
}
