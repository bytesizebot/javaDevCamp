package za.co.entelect.java_devcamp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification {
    private String recipient;
    private String subject;
    private String notificationContent;
}