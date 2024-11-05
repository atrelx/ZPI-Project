package com.zpi.amoz.services;

import com.zpi.amoz.models.Address;
import com.zpi.amoz.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Optional<Address> findById(UUID id) {
        return addressRepository.findById(id);
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public boolean deleteById(UUID id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

