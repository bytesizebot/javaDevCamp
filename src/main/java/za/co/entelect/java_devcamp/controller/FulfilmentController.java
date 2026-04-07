package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.rabbitmq.MessageProducer;
import za.co.entelect.java_devcamp.serviceinterface.IFulfilmentService;

@RestController
@RequestMapping("fulfilment-checks")
@Tag(name = "Fulfillment Checks", description = "Fulfilment checks management API")
public class FulfilmentController {

    @Autowired
    private MessageProducer messageProducer;

    private final IFulfilmentService iFulfilmentService;

    public FulfilmentController(IFulfilmentService iFulfilmentService) {
        this.iFulfilmentService = iFulfilmentService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message){
        messageProducer.sendMessage(message);
        return "Message sent to RabbitMQ: +" + message;
    }

    @PostMapping("/send-test")
    public String sendTest(@RequestBody String message) {
        messageProducer.sendTestMessage(message);
        return "Sent test message directly to queue: " + message;
    }


}
