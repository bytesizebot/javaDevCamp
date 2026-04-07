package za.co.entelect.java_devcamp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.model.Notification;
import za.co.entelect.java_devcamp.serviceinterface.INotificationService;
import za.co.entelect.java_devcamp.util.MaskingUtils;

@Slf4j
@Service
public class NotificationService implements INotificationService {

    @Autowired
    private JavaMailSender mailSender;



    private final String template;

    public NotificationService(String template) {

        this.template = template;
    }


    public String buildEmail(String content) {
        return template.replace("{message}", content);
    }

    @Override
    public void sendNotification(Notification notification) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = buildEmail(notification.getNotificationContent());

            helper.setFrom("noreply@producthouse.com");
            helper.setTo(notification.getRecipient());
            helper.setSubject(notification.getSubject());
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info("Notification sent to " + MaskingUtils.maskEmail(notification.getRecipient()));
        } catch (MessagingException e) {
            log.error("Failed to send notification to " + notification.getRecipient(), e);
        }
    }
}
