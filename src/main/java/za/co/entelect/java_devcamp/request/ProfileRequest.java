package za.co.entelect.java_devcamp.request;

import za.co.entelect.java_devcamp.entity.CustomerAccounts;
import za.co.entelect.java_devcamp.entity.QualifyingAccounts;

import java.util.List;

public record ProfileRequest (
        String username,
        String firstName,
        String lastName,
        String idNumber,
        Long customerTypeId,
        List<CustomerAccounts> accounts
){

}
