package com.zpi.amoz.services;

import com.zpi.amoz.models.ProductOrderItem;
import com.zpi.amoz.repository.ProductOrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductOrderItemService {

    private final ProductOrderItemRepository productOrderItemRepository;

    @Autowired
    public ProductOrderItemService(ProductOrderItemRepository productOrderItemRepository) {
        this.productOrderItemRepository = productOrderItemRepository;
    }

    public List<ProductOrderItem> findAll() {
        return productOrderItemRepository.findAll();
    }

    public Optional<ProductOrderItem> findById(UUID id) {
        return productOrderItemRepository.findById(id);
    }

    public ProductOrderItem save(ProductOrderItem productOrderItem) {
        return productOrderItemRepository.save(productOrderItem);
    }

    public void deleteById(UUID id) {
        productOrderItemRepository.deleteById(id);
    }
}

