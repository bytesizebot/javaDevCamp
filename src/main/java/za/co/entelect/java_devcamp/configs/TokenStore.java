package za.co.entelect.java_devcamp.configs;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TokenStore {
    private volatile String token;


    public synchronized void setToken(String token) {
        this.token = token;
    }

    public synchronized String getToken() {
        if (token == null ) {
            return null;
        }
        return token;
    }

}
