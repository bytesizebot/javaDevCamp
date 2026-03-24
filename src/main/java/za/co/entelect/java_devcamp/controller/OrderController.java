package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.dto.OrderDto;
import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.request.UpdateOrderRequest;
import za.co.entelect.java_devcamp.mapper.OrderMapper;
import za.co.entelect.java_devcamp.request.OrderRequest;
import za.co.entelect.java_devcamp.service.IOrderService;

import java.util.List;

@RestController
@RequestMapping("orders")
@Tag(name = "Order", description = "Order management API")
public class OrderController {
    
    private final IOrderService iOrderService;
    private final OrderMapper orderMapper;

    public OrderController(IOrderService iOrderService, OrderMapper orderMapper) {
        this.iOrderService = iOrderService;
        this.orderMapper = orderMapper;
    }

    @Operation(summary="Get All Orders")
    @GetMapping("/")
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<OrderDto> OrderDto = iOrderService.getOrders();
        return ResponseEntity.ok(OrderDto);
    }

    @Operation(summary="Get Order By Id")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id){
        OrderDto OrderDto = iOrderService.getOrderById(id);
        return ResponseEntity.ok(OrderDto);
    }

    @PostMapping("")
    @Operation(summary = "create a product order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){
        try {
            OrderDto orderDto = orderMapper.toOrderDto(iOrderService.createOrder(orderRequest.getCustomerEmail(), orderRequest.getProductId()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(orderDto);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Updating the status of an order")
    public String updateOrderStatus(@RequestBody UpdateOrderRequest updateRequest){
        try {
            Order updatedOrder = iOrderService.updateOrderStatus(updateRequest.getOrderId(), updateRequest.getNewStatus());
            return ("Order status successfully changed to: " + updateRequest.getNewStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
