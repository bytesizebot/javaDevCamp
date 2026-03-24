package za.co.entelect.java_devcamp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.OrderDto;
import za.co.entelect.java_devcamp.entity.*;
import za.co.entelect.java_devcamp.exception.ProductTakeupFailedException;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.OrderMapper;
import za.co.entelect.java_devcamp.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final IProfileService iProfileService;
    private final IProductService iProductService;
    private final IEligibilityService iEligibilityService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, IProfileService iProfileService, IProductService iProductService, IEligibilityService iEligibilityService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.iProfileService = iProfileService;
        this.iProductService = iProductService;
        this.iEligibilityService = iEligibilityService;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toOrderDto).toList();
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Order not found with id ") + id));
        return orderMapper.toOrderDto(order);
    }

    @Override
    public Order createOrder(String customerEmail, Long productId) {
        log.info("Creating order because the customer is eligible for a product");

        Product product = iProductService.getProductById(productId);
        Profile customerProfile = iProfileService.getProfileByUserName(customerEmail);
        Eligibility eligibility = iEligibilityService.getEligibilityByProductIdAndCustomerId(customerProfile.getProfileId(), productId);
        if (!eligibility.getResult()) {
            throw new ProductTakeupFailedException("Customer cannot take up product. Ensure customer is eligible first.");
        } else {
            if (customerProfile == null) {
                throw new ResourceNotFoundException("Customer profile not found");
            }
            Order productOrder = new Order();
            productOrder.setCustomerId(customerProfile.getProfileId());
            productOrder.setCreatedAt(LocalDateTime.now());
            productOrder.setOrderStatus(Status.PENDING);

            OrderItem item = new OrderItem();
            item.setProduct(product);

            productOrder.addProducts(item);

            orderRepository.save(productOrder);
            return productOrder;
        }
    }

    @Override
    public Order updateOrderStatus(Long orderId, Status newStatus) {
        Order order = orderMapper.toOrderEntity(getOrderById(orderId));
        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }
}
