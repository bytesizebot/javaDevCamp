package za.co.entelect.java_devcamp.serviceinterface;

import za.co.entelect.java_devcamp.request.FulfilmentRequest;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;

public interface IOrderOrchestrationService {

    public FulfilmentResponse completeTypeAProcess(FulfilmentRequest request);
    public FulfilmentResponse completeTypeBProcess(FulfilmentRequest request);
    public FulfilmentResponse completeTypeCProcess(FulfilmentRequest request);

}
