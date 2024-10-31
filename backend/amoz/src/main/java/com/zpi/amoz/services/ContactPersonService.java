package com.zpi.amoz.services;

import com.zpi.amoz.models.ContactPerson;
import com.zpi.amoz.repository.ContactPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;

    @Autowired
    public ContactPersonService(ContactPersonRepository contactPersonRepository) {
        this.contactPersonRepository = contactPersonRepository;
    }

    public List<ContactPerson> findAll() {
        return contactPersonRepository.findAll();
    }

    public Optional<ContactPerson> findById(UUID id) {
        return contactPersonRepository.findById(id);
    }

    public ContactPerson save(ContactPerson contactPerson) {
        return contactPersonRepository.save(contactPerson);
    }

    public void deleteById(UUID id) {
        contactPersonRepository.deleteById(id);
    }
}

