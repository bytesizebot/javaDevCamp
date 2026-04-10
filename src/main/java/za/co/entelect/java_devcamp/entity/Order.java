package za.co.entelect.java_devcamp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Order", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column
    private Long customerId;

    @Column
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status orderStatus;

    @Column
    private String contractUrl;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(Long customerId, LocalDateTime createdAt, Status orderStatus, String contractUrl, List<OrderItem> orderItems) {
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
        this.contractUrl = contractUrl;
    }

    public void addProducts(OrderItem item){
        orderItems.add(item);
        item.setOrder(this);
    }

}
