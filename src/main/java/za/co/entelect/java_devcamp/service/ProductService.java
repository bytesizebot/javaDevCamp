package za.co.entelect.java_devcamp.service;

import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.customerdto.AccountTypeDto;
import za.co.entelect.java_devcamp.customerdto.AccountsDto;
import za.co.entelect.java_devcamp.customerdto.CustomerDto;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.entity.QualifyingAccounts;
import za.co.entelect.java_devcamp.entity.QualifyingCustomerTypes;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.ProductMapper;
import za.co.entelect.java_devcamp.repository.ProductRepository;
import za.co.entelect.java_devcamp.repository.QualifyingAccountsRepository;
import za.co.entelect.java_devcamp.repository.QualifyingCustomerTypesRepository;
import za.co.entelect.java_devcamp.webclient.CISWebService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final QualifyingAccountsRepository qualifyingAccountsRepository;
    private final QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository;
    private final ProductMapper productMapper;
    private final CISWebService cisWebService;

    public ProductService(ProductRepository productRepository, QualifyingAccountsRepository qualifyingAccountsRepository, QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository, ProductMapper productMapper, CISWebService cisWebService) {
        this.productRepository = productRepository;
        this.qualifyingAccountsRepository = qualifyingAccountsRepository;
        this.qualifyingCustomerTypesRepository = qualifyingCustomerTypesRepository;
        this.productMapper = productMapper;
        this.cisWebService = cisWebService;
    }

    @Override
    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toDto).toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Product not found with id: ") + id));
        return productMapper.toDto(product);
    }

    @Override
    public boolean isEligibleForProduct(String customerEmail, Long productId, String jwtToken) {

        //get customer by Email from CIS
        CustomerDto customer = cisWebService.getCustomerByEmail(customerEmail, jwtToken);

        //using customer, get the customerTypeId && customerAccounts[]
        Long customerTypeId = Long.valueOf(customer.getCustomerTypeId());
        List<AccountsDto> customerAccounts = customer.getCustomerAccounts();

        //get customer accounts id
        List<Integer> accountIds = Optional.ofNullable(customerAccounts)
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(AccountsDto::getAccountType)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(AccountTypeDto::getId)
                .filter(Objects::nonNull)
                .toList();

        boolean hasAQualifyingAccount = false;

        //query qualifying accounts table for product id; return account ids;
        for (Integer accountId : accountIds) {
            if (accountId == null) continue;

            QualifyingAccounts qualifyingAccounts = qualifyingAccountsRepository.findByAccountIdAndProductProductId(accountId.longValue(), productId);

            if (qualifyingAccounts != null && qualifyingAccounts.getAccountId() != null &&
                    qualifyingAccounts.getAccountId().equals(accountId.longValue())) {
                hasAQualifyingAccount = true;
            }
        }
        //query qualifying customer types table with product id; return customer type ids;
        QualifyingCustomerTypes qualifyingCustomerTypes = qualifyingCustomerTypesRepository.findByCustomerTypesIdAndProductProductId(customerTypeId, productId);

        //check if customer type id in qualifyingCustomerTypes &&
        return ((qualifyingCustomerTypes != null) && (hasAQualifyingAccount));

        //check if customer account id in qualifyingAccounts
        //return true/ false;
    }
}
