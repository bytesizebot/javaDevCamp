package za.co.entelect.java_devcamp.model;

import lombok.Getter;
import lombok.Setter;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;

@Getter
@Setter
public class TypeAMessage {
    private Result result;
    private FulfilmentResponse response;

    public TypeAMessage() {}

    public Result getResult() { return result; }
    public void setResult(Result result) { this.result = result; }

    public FulfilmentResponse getResponse() { return response; }
    public void setResponse(FulfilmentResponse response) { this.response = response; }
}
