package com.zpi.amoz.services;

import com.zpi.amoz.models.ProductAttribute;
import com.zpi.amoz.repository.ProductAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;

    @Autowired
    public ProductAttributeService(ProductAttributeRepository productAttributeRepository) {
        this.productAttributeRepository = productAttributeRepository;
    }

    public List<ProductAttribute> findAll() {
        return productAttributeRepository.findAll();
    }

    public Optional<ProductAttribute> findById(UUID id) {
        return productAttributeRepository.findById(id);
    }

    public ProductAttribute save(ProductAttribute productAttribute) {
        return productAttributeRepository.save(productAttribute);
    }

    public void deleteById(UUID id) {
        productAttributeRepository.deleteById(id);
    }
}
