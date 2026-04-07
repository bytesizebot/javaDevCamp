package za.co.entelect.java_devcamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.OrderDto;
import za.co.entelect.java_devcamp.dto.OrderItemDto;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.*;
import za.co.entelect.java_devcamp.exception.ProductTakeupFailedException;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.OrderMapper;
import za.co.entelect.java_devcamp.rabbitmq.MessageProducer;
import za.co.entelect.java_devcamp.repository.OrderRepository;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;
import za.co.entelect.java_devcamp.serviceinterface.*;
import za.co.entelect.java_devcamp.util.ActionCompletedFulfilmentChecks;
import za.co.entelect.java_devcamp.webclient.CISWebService;
import za.co.entelect.java_devcamp.webclientdto.CustomerDto;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final IProductService iProductService;
    private final IEligibilityService iEligibilityService;
    private final OrderMapper orderMapper;
    private final MessageProducer messageProducer;
    private final CISWebService cisWebService;
    private final IFulfilmentService iFulfilmentService;
    private final IDocumentService iDocumentService;

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
        CustomerDto customer = cisWebService.getCustomerByEmail(customerEmail);

        Eligibility eligibility = iEligibilityService.getEligibilityByProductIdAndCustomerId(customer.getId(), productId);

        if (!eligibility.getResult()) {
            throw new ProductTakeupFailedException("Customer cannot take up product. Ensure customer is eligible first.");
        } else {
            if (customer == null) {
                throw new ResourceNotFoundException("Customer profile not found");
            }
            Order productOrder = new Order();

            productOrder.setCustomerId(customer.getId());
            productOrder.setCreatedAt(LocalDateTime.now());
            productOrder.setOrderStatus(Status.PENDING);

            OrderItem item = new OrderItem();
            item.setProduct(product);

            productOrder.addProducts(item);

            orderRepository.save(productOrder);

            iFulfilmentService.determineFulfillmentCheck(productOrder, customer.getId(), customer.getIdNumber());
            messageProducer.sendMessage("A new product needs fulfilment for customer: " + customerEmail);
            return productOrder;
        }
    }

    @Override
    public Order updateOrderStatus(Long orderId, Status newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setOrderStatus(newStatus);

        return orderRepository.save(order);
    }

    @EventListener
    public void handleCompletedFulfilmentCheck(ActionCompletedFulfilmentChecks actionCompletedFulfilmentChecks){
        completeOrder(actionCompletedFulfilmentChecks.getResponse());
    }
    @Override
    public void completeOrder(FulfilmentResponse fulfillmentResponse) {
        log.info("Completing order for orderId: {}", fulfillmentResponse.getOrderId());
        if(fulfillmentResponse.isSuccessful()){
            updateOrderStatus(fulfillmentResponse.getOrderId(), Status.APPROVED);
            log.info("Order for customer with ID: {} has been approved! Yay.",fulfillmentResponse.getCustomerId());

            //Generate contract Url call document service
            Long orderId = fulfillmentResponse.getOrderId();

            OrderDto customerOrder = getOrderById(orderId);
            CustomerDto customerProfile = cisWebService.getCustomerById(customerOrder.customerId());
            List<OrderItemDto> orderItems = getOrderById(orderId).orderItemsDto();
            List<ProductDto> products = orderItems.stream()
                    .map(OrderItemDto::product)
                    .toList();

            iDocumentService.generateCustomerContract(products, customerProfile);
        }

        updateOrderStatus(fulfillmentResponse.getOrderId(), Status.DECLINED);
        log.info("Order for customer with ID: {} has been declined. Order request unsuccessful.",fulfillmentResponse.getCustomerId());
    }
}
