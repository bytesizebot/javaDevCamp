package za.co.entelect.java_devcamp.customerdto;

import java.util.List;

public class AccountsDto {

    private List<AccountTypeDto> accountType;

    public AccountsDto(){}

    public AccountsDto(List<AccountTypeDto> accountType){
        this.accountType = accountType;
    }

    public List<AccountTypeDto> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<AccountTypeDto> accountType) {
        this.accountType = accountType;
    }
}
