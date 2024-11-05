package com.zpi.amoz.services;

import com.zpi.amoz.models.CustomerB2B;
import com.zpi.amoz.repository.CustomerB2BRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerB2BService {

    private final CustomerB2BRepository customerB2BRepository;

    @Autowired
    public CustomerB2BService(CustomerB2BRepository customerB2BRepository) {
        this.customerB2BRepository = customerB2BRepository;
    }

    public List<CustomerB2B> findAll() {
        return customerB2BRepository.findAll();
    }

    public Optional<CustomerB2B> findById(UUID id) {
        return customerB2BRepository.findById(id);
    }

    public CustomerB2B save(CustomerB2B customerB2B) {
        return customerB2BRepository.save(customerB2B);
    }

    public boolean deleteById(UUID id) {
        if (customerB2BRepository.existsById(id)) {
            customerB2BRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
