package za.co.entelect.java_devcamp.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.entelect.java_devcamp.dto.OrderDto;
import za.co.entelect.java_devcamp.dto.OrderItemDto;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.entity.OrderItem;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.entity.Status;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.OrderMapper;
import za.co.entelect.java_devcamp.rabbitmq.MessageProducer;
import za.co.entelect.java_devcamp.rabbitmq.OrderProducer;
import za.co.entelect.java_devcamp.repository.OrderRepository;
import za.co.entelect.java_devcamp.service.OrderService;
import za.co.entelect.java_devcamp.serviceinterface.*;
import za.co.entelect.java_devcamp.webclient.CISWebService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private MessageProducer messageProducer;
    @Mock
    private CISWebService cisWebService;

    @InjectMocks
    private OrderService orderService;
    @Mock
    private IProductService iProductService;
    @Mock
    private IEligibilityService iEligibilityService;
    @Mock
    private IDocumentService iDocumentService;
    @Mock
    private INotificationService iNotificationService;
    @Mock
    private IProfileService iProfileService;
    @Mock
    private OrderProducer orderProducer;
    private Order mockOrderEntity;
    private OrderDto mockOrderDto;
    private OrderItem mockOrderItemsEntity;
    private OrderItemDto mockOrderItemDto;
    private Product mockProductEntity;


    @BeforeEach
    public void setup() {
        orderService = new OrderService(orderRepository, iProductService, iEligibilityService, orderMapper, messageProducer, cisWebService,  iDocumentService, iNotificationService, iProfileService, orderProducer);
        mockProductEntity = Product.builder()
                .productId(1L)
                .Name("Product")
                .Description("Description of the first product")
                .Price(1000.00F)
                .ImageUrl("http://image.com")
                .build();

        mockOrderItemsEntity = OrderItem
                .builder()
                .orderItemId(3L)
                .product(mockProductEntity)
                .build();

        mockOrderEntity = Order
                .builder()
                .orderId(1L)
                .customerId(4L)
                .createdAt(LocalDateTime.now())
                .orderStatus(Status.PENDING)
                .contractUrl("")
                .orderItems(List.of(mockOrderItemsEntity))
                .build();

        mockOrderItemDto = new OrderItemDto(new ProductDto( "Product", " Description of the first product", 5000.00F, "http://url8490"));

        mockOrderDto = new OrderDto(8L, LocalDateTime.now(), Status.PENDING, "", List.of(mockOrderItemDto));

    }

    @Test
    public void testGetAllOrders_WhenListOfOrdersExists_thenReturnAllOrders() {
        Product newProduct = Product.builder()
                .productId(3L)
                .Name("Product three")
                .Description("Description of the third product")
                .Price(1000.00F)
                .ImageUrl("http://image.com")
                .build();

        OrderItem anotherItem = OrderItem
                .builder()
                .orderItemId(3L)
                .product(newProduct)
                .build();

        Order anotherOrder = Order
                .builder()
                .orderId(2L)
                .customerId(8L)
                .createdAt(LocalDateTime.now())
                .orderStatus(Status.DECLINED)
                .contractUrl("")
                .orderItems(List.of(mockOrderItemsEntity, anotherItem))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(mockOrderEntity, anotherOrder));

        List<OrderDto> ordersList = orderService.getOrders();

        assertEquals(2, ordersList.size());
        verify(orderRepository, times(1)).findAll();

    }

    @Test
    public void testGetOrderById_WhenOrderIdIsPresent_ThenReturnOrder() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(mockOrderEntity));
        when(orderMapper.toOrderDto(mockOrderEntity))
                .thenReturn(mockOrderDto);

        OrderDto foundOrder = orderService.getOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(8L, foundOrder.customerId());
    }

    @Test
    public void testGetOrderById_WhenOrderIdIsNotPresent_ThenThrowException() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderById(1L);
        });

        verify(orderMapper, never()).toOrderDto(any());
    }

}