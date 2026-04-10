package za.co.entelect.java_devcamp.soap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
@Slf4j
@Component
public class SoapMessageSender extends HttpUrlConnectionMessageSender {
    private String user = "user";
    private String password = "password";

    @Override
    protected void prepareConnection(HttpURLConnection connection) throws IOException {
        String userpassword = user + ":" + password;
        String encodedAuthorization = java.util.Base64.getEncoder()
                .encodeToString(userpassword.getBytes(StandardCharsets.UTF_8));

        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

        connection.setRequestProperty("SOAPAction", "");
        super.prepareConnection(connection);
    }
}
