package com.zpi.amoz.services;

import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.CategoryRepository;
import com.zpi.amoz.repository.ProductRepository;
import com.zpi.amoz.repository.ProductVariantRepository;
import com.zpi.amoz.repository.UserRepository;
import com.zpi.amoz.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;


@Service
public class AuthorizationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;


    public boolean hasPermissionToUpdateProduct(UserPrincipal userPrincipal, UUID productId) {
        UUID userCompanyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("User is not in any company"))
                .getCompanyId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return product.getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToUpdateProductVariant(UserPrincipal userPrincipal, UUID productVariantId) {
        UUID userCompanyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("User is not in any company"))
                .getCompanyId();

        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found"));

        return productVariant.getProduct().getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToUpdateCategory(UserPrincipal userPrincipal, UUID categoryId) {
        UUID userCompanyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("User is not in any company"))
                .getCompanyId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Product product = productRepository.findByCategory_CategoryId(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Product with given category not found: " + categoryId));

        return product.getCompany().getCompanyId().equals(userCompanyId);
    }
}
