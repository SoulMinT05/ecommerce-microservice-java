package org.soulmin.ecommerce.order;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.soulmin.ecommerce.customer.CustomerClient;
import org.soulmin.ecommerce.exception.BusinessException;
import org.soulmin.ecommerce.kafka.OrderConfirmation;
import org.soulmin.ecommerce.kafka.OrderProducer;
import org.soulmin.ecommerce.orderline.OrderLineRequest;
import org.soulmin.ecommerce.orderline.OrderLineService;
import org.soulmin.ecommerce.payment.PaymentClient;
import org.soulmin.ecommerce.payment.PaymentRequest;
import org.soulmin.ecommerce.product.ProductClient;
import org.soulmin.ecommerce.product.PurchaseRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest orderRequest) {
        var customer = this.customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order for non-existing customer"));

        var purchasedProducts = this.productClient.purchaseProducts(orderRequest.products());

        var order = this.orderRepository.save(orderMapper.toOrder(orderRequest));

        for (PurchaseRequest purchaseRequest: orderRequest.products()) {
            orderLineService.saveOrderLine(
                new OrderLineRequest(
                    null,
                    order.getId(),
                    purchaseRequest.productId(),
                    purchaseRequest.quantity()
                )
            );
        }

        paymentClient.requestOrderPayment(
                new PaymentRequest(
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        orderRequest.id(),
                        orderRequest.reference(),
                        customer
                )
        );

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequest.reference(),
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::fromOrder)
                .toList();
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException("No order found with provided ID: " + orderId));
    }
}
