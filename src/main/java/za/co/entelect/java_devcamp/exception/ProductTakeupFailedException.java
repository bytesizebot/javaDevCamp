package za.co.entelect.java_devcamp.exception;

public class ProductTakeupFailedException extends  RuntimeException{
    public ProductTakeupFailedException(String message){
        super(message);
    }
}
