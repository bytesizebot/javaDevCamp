package za.co.entelect.java_devcamp.request;

public record ProfileRequest (
        String username,
        String firstName,
        String lastName,
        String idNumber,
        Long customerTypeId
){

}
