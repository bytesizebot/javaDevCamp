package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.entelect.java_devcamp.model.Notification;
import za.co.entelect.java_devcamp.serviceinterface.INotificationService;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.util.MaskingUtils;

@RestController
@RequestMapping("/notification")
@Tag(name = "Notification")
public class NotificationController {

    @Autowired
    private INotificationService iNotificationService;

    @PostMapping
    public void sendNotification(@RequestBody Notification notification) {
        iNotificationService.sendNotification(notification);
    }
}
