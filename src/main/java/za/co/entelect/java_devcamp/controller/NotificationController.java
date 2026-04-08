package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.java_devcamp.model.Notification;
import za.co.entelect.java_devcamp.serviceinterface.INotificationService;

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
