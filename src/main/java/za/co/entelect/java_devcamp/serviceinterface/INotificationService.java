package za.co.entelect.java_devcamp.serviceinterface;

import za.co.entelect.java_devcamp.model.Notification;

public interface INotificationService {

    String buildEmail(String content);
    void sendNotification(Notification notification);
}

