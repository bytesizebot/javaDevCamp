package za.co.entelect.java_devcamp.service;

import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.OrderDto;
import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.OrderMapper;
import za.co.entelect.java_devcamp.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException(("Order not found with id ") + id ));
        return orderMapper.toOrderDto(order);
    }
}
