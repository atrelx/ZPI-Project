package com.zpi.amoz.services;

import com.zpi.amoz.models.Address;
import com.zpi.amoz.repository.AddressRepository;
import com.zpi.amoz.requests.AddressCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional

    public Address createAddress(AddressCreateRequest request) {
        Address address = new Address();
        address.setCity(request.city());
        address.setStreet(request.street());
        address.setStreetNumber(request.streetNumber());
        address.setApartmentNumber(request.apartmentNumber());
        address.setPostalCode(request.postalCode());
        request.additionalInformation().ifPresent(address::setAdditionalInformation);
        return addressRepository.save(address);
    }

    @Transactional

    public Address updateAddress(UUID addressId, AddressCreateRequest request) {
        Address address = addressRepository.findById(addressId)
                        .orElseThrow(() -> new EntityNotFoundException("Could not find address for given id: " + addressId));
        address.setCity(request.city());
        address.setStreet(request.street());
        address.setStreetNumber(request.streetNumber());
        address.setApartmentNumber(request.apartmentNumber());
        address.setPostalCode(request.postalCode());
        request.additionalInformation()
                .ifPresentOrElse(address::setAdditionalInformation, () -> address.setAdditionalInformation(null));
        return addressRepository.save(address);
    }
}

