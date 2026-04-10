package za.co.entelect.java_devcamp.configs;

import org.springframework.stereotype.Component;

@Component
public class TokenStore {
    private volatile String token;
    private volatile String user_role;

    public synchronized void setToken(String token) {
        this.token = token;
    }

    public synchronized void setRole(String user_role) {
        this.user_role = user_role;
    }

    public synchronized String getToken() {
        if (token == null) {
            return null;
        }
        return token;
    }

    public synchronized String getAuthority() {
        if (user_role == null) {
            return null;
        }
        return user_role;
    }

}
