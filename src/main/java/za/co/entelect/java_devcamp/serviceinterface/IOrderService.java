package za.co.entelect.java_devcamp.serviceinterface;

import za.co.entelect.java_devcamp.dto.OrderDto;
import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.entity.Status;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;

import java.util.List;
public interface IOrderService {

    List<OrderDto> getOrders();

    OrderDto getOrderById(Long id);

    Order createOrder(String customerEmail, Long productId );

    Order updateOrderStatus(Long orderId, Status newStatus);

    void completeOrder(FulfilmentResponse fulfillmentResponse);

}
