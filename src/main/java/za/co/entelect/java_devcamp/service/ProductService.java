package za.co.entelect.java_devcamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.Eligibility;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.entity.QualifyingAccounts;
import za.co.entelect.java_devcamp.entity.QualifyingCustomerTypes;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.ProductMapper;
import za.co.entelect.java_devcamp.repository.EligibilityRepository;
import za.co.entelect.java_devcamp.repository.ProductRepository;
import za.co.entelect.java_devcamp.repository.QualifyingAccountsRepository;
import za.co.entelect.java_devcamp.repository.QualifyingCustomerTypesRepository;
import za.co.entelect.java_devcamp.serviceinterface.IProductService;
import za.co.entelect.java_devcamp.webclient.CISWebService;
import za.co.entelect.java_devcamp.webclientdto.AccountTypeDto;
import za.co.entelect.java_devcamp.webclientdto.CustomerDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final QualifyingAccountsRepository qualifyingAccountsRepository;
    private final QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository;
    private final ProductMapper productMapper;
    private final CISWebService cisWebService;
    private final EligibilityRepository eligibilityRepository;

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

        if (eligibilityRepository.existsByCustomerIdAndProductId(customer.getId(), productId)) {
            return eligibilityRepository.findByCustomerIdAndProductId(customer.getId(), productId).getResult();
        }
        //To do: check token n user at the same time

        Long customerTypeId = customer.getCustomerType().getId().longValue();

        List<AccountTypeDto> customerAccounts = customer.getCustomerAccounts();

        List<Integer> accountIds = Optional.ofNullable(customerAccounts)
                .orElse(Collections.emptyList())
                .stream()
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

        Eligibility eligible = new Eligibility(productId, customer.getId(), isEligible);

        eligibilityRepository.save(eligible);
        return isEligible;
    }
}
