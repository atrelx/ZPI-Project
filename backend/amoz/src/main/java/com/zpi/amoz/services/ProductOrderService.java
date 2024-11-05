package com.zpi.amoz.services;

import com.zpi.amoz.models.ProductOrder;
import com.zpi.amoz.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;

    @Autowired
    public ProductOrderService(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    public List<ProductOrder> findAll() {
        return productOrderRepository.findAll();
    }

    public Optional<ProductOrder> findById(UUID id) {
        return productOrderRepository.findById(id);
    }

    public ProductOrder save(ProductOrder productOrder) {
        return productOrderRepository.save(productOrder);
    }

    public boolean deleteById(UUID id) {
        if (productOrderRepository.existsById(id)) {
            productOrderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

