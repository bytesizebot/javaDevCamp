package za.co.entelect.java_devcamp.mapper;

import org.springframework.stereotype.Component;
import za.co.entelect.java_devcamp.dto.OrderDto;
import za.co.entelect.java_devcamp.dto.OrderItemDto;
import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.entity.OrderItem;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.repository.ProductRepository;

@Component
public class OrderMapper {

    private final ProductRepository productRepository;

    public OrderMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderItemDto toOrderItemDto(OrderItem orderItem){
        return new OrderItemDto(
                orderItem.getProduct().getProductId(),
                orderItem.getProduct().getName(),
                orderItem.getProduct().getDescription()
        );
    }

    public OrderItem toOrderItemEntity(OrderItemDto orderItemDto){
        Product product = productRepository.findById(orderItemDto.productId())
                .orElseThrow(() -> new ResourceNotFoundException(("Product not found with id: ") + orderItemDto.productId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);

        return orderItem;
    }
    public OrderDto toOrderDto(Order order){
        return new OrderDto(
                order.getCustomerId(),
                order.getCreatedAt(),
                order.getOrderStatus(),
                order.getContractUrl(),
                order.getOrderItems().stream().map(this::toOrderItemDto).toList()
        );
    }

    public Order toOrderEntity(OrderDto orderDto){
        return new Order(
                orderDto.customerId(),
                orderDto.createdAt(),
                orderDto.orderStatus(),
                orderDto.contractUrl(),
                orderDto.orderItemsDto().stream().map(this::toOrderItemEntity).toList()
        );

    }
}
