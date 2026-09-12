package org.soulmin.ecommerce.order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.soulmin.ecommerce.customer.CustomerClient;
import org.soulmin.ecommerce.exception.BusinessException;
import org.soulmin.ecommerce.orderline.OrderLineRequest;
import org.soulmin.ecommerce.orderline.OrderLineService;
import org.soulmin.ecommerce.product.ProductClient;
import org.soulmin.ecommerce.product.PurchaseRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;

    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public Integer createOrder(OrderRequest orderRequest) {
        var customer = this.customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order for non-existing customer"));

        this.productClient.purchaseProducts(orderRequest.products());

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
        return null;
    }
}
