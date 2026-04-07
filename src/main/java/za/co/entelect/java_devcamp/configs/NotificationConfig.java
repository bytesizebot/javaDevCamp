package za.co.entelect.java_devcamp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class NotificationConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(1025);

        mailSender.setUsername("");
        mailSender.setPassword("");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");

        return mailSender;
    }

    @Bean
    public String templateSimpleMessage() {
        String message =
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                          <meta charset="UTF-8">
                          <title>Notification Email</title>
                          <style>
                            body {
                              font-family: Arial, sans-serif;
                              background-color: #f8f8f8;
                              margin: 0;
                              padding: 0;
                            }
                            .email-container {
                              max-width: 600px;
                              margin: 40px auto;
                              background-color: #ffffff;
                              padding: 20px 30px;
                              border: 1px solid #e0e0e0;
                              border-radius: 8px;
                              box-shadow: 0 2px 4px rgba(0,0,0,0.05);
                            }
                            .email-header {
                              font-size: 18px;
                              font-weight: bold;
                              margin-bottom: 20px;
                              color: #333333;
                            }
                            .email-body {
                              font-size: 16px;
                              line-height: 1.6;
                              color: #555555;
                            }
                            .email-footer {
                              margin-top: 30px;
                              font-size: 14px;
                              color: #777777;
                            }
                          </style>
                        </head>
                        <body>
                          <div class="email-container">
                            <div class="email-header">
                              Notification
                            </div>
                            <div class="email-body">
                              Good day,<br><br>
                              This email is to notify you that <strong>{message}</strong>.<br><br>
                              Kind regards,<br>
                              PH
                            </div>
                            <div class="email-footer">
                              &copy; 2026 PH. All rights reserved.
                            </div>
                          </div>
                        </body>
                        </html>
                        """;
        return message;
    }
}
