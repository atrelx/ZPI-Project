package com.zpi.amoz.services;


import com.zpi.amoz.models.CustomerB2C;
import com.zpi.amoz.repository.CustomerB2CRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerB2CService {

    private final CustomerB2CRepository customerB2CRepository;

    @Autowired
    public CustomerB2CService(CustomerB2CRepository customerB2CRepository) {
        this.customerB2CRepository = customerB2CRepository;
    }

    public List<CustomerB2C> findAll() {
        return customerB2CRepository.findAll();
    }

    public Optional<CustomerB2C> findById(UUID id) {
        return customerB2CRepository.findById(id);
    }

    public CustomerB2C save(CustomerB2C customerB2C) {
        return customerB2CRepository.save(customerB2C);
    }

    public boolean deleteById(UUID id) {
        if (customerB2CRepository.existsById(id)) {
            customerB2CRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

