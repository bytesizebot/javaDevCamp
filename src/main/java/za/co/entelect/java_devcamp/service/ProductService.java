package za.co.entelect.java_devcamp.service;

import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.customerdto.AccountTypeDto;
import za.co.entelect.java_devcamp.customerdto.AccountsDto;
import za.co.entelect.java_devcamp.customerdto.CustomerDto;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.*;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.ProductMapper;
import za.co.entelect.java_devcamp.repository.*;
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
    private final ProfileRepository profileRepository;
    private final EligibilityRepository eligibilityRepository;

    public ProductService(ProductRepository productRepository, QualifyingAccountsRepository qualifyingAccountsRepository, QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository, ProductMapper productMapper, CISWebService cisWebService, ProfileRepository profileRepository, EligibilityRepository eligibilityRepository) {
        this.productRepository = productRepository;
        this.qualifyingAccountsRepository = qualifyingAccountsRepository;
        this.qualifyingCustomerTypesRepository = qualifyingCustomerTypesRepository;
        this.productMapper = productMapper;
        this.cisWebService = cisWebService;
        this.profileRepository = profileRepository;
        this.eligibilityRepository = eligibilityRepository;
    }

    @Override
    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toDto).toList();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Product not found with id: ") + id));

    }

    @Override
    public boolean isEligibleForProduct(String customerEmail, Long productId) {
        CustomerDto customer = cisWebService.getCustomerByEmail(customerEmail);

        if(eligibilityRepository.existsByCustomerIdAndProductId(customer.getId(), productId)){
            return eligibilityRepository.findByCustomerIdAndProductId(customer.getId(), productId).getResult();
        }
        //check token n user at the same time
        String customerType = customer.getCustomerType().getName();

        Profile profile = profileRepository.findByEmailAddress(customerEmail)
               .orElseThrow(() -> new ResourceNotFoundException("Profile not found with username: " + customerEmail));
        Long customerTypeId = profile.getCustomerTypeId();

        List<AccountsDto> customerAccounts = customer.getCustomerAccounts();

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
        for (Integer accountId : accountIds) {
            if (accountId == null) continue;

            QualifyingAccounts qualifyingAccounts = qualifyingAccountsRepository.findByAccountIdAndProductProductId(accountId.longValue(), productId);

            if (qualifyingAccounts != null && qualifyingAccounts.getAccountId() != null &&
                    qualifyingAccounts.getAccountId().equals(accountId.longValue())) {
                hasAQualifyingAccount = true;
            }
        }
        QualifyingCustomerTypes qualifyingCustomerTypes = qualifyingCustomerTypesRepository.findByCustomerTypesIdAndProductProductId(customerTypeId, productId);
        boolean isEligible = ((qualifyingCustomerTypes != null) && (hasAQualifyingAccount));

        Eligibility eligible = new Eligibility(productId,profile.getProfileId(), isEligible);

        eligibilityRepository.save(eligible);
        return isEligible;
    }
}
