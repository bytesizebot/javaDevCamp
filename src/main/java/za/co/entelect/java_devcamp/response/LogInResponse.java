package za.co.entelect.java_devcamp.response;

public class LogInResponse {
    private String token;
    private String username;
    private String message;

    public LogInResponse(String token, String username, String message) {
        this.token = token;
        this.username = username;
        this.message = message;
    }

    public LogInResponse(String message) {
        this.message = message;
    }

    public LogInResponse() {
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

