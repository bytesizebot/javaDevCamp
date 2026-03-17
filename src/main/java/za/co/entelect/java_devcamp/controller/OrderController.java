package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.java_devcamp.dto.OrderDto;
import za.co.entelect.java_devcamp.service.IOrderService;

import java.util.List;

@RestController
@RequestMapping("orders")
@Tag(name = "Order", description = "Order management API")
public class OrderController {
    
    private final IOrderService iOrderService;

    public OrderController(IOrderService iOrderService) {
        this.iOrderService = iOrderService;
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
}
