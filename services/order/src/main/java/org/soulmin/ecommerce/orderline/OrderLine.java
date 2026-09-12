package org.soulmin.ecommerce.orderline;

import jakarta.persistence.*;
import lombok.*;
import org.soulmin.ecommerce.order.Order;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class OrderLine {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Integer productId;
    private double quantity;

}
