package za.co.entelect.java_devcamp.model;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification {
    private String recipient;
    private String subject;
    private String notificationContent;
}