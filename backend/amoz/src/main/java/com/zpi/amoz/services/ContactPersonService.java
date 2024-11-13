package com.zpi.amoz.services;

import com.zpi.amoz.models.ContactPerson;
import com.zpi.amoz.repository.ContactPersonRepository;
import com.zpi.amoz.requests.ContactPersonCreateRequest;
import jakarta.persistence.EntityNotFoundException;
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

    public Optional<ContactPerson> findByEmailAddress(String emailAddress) { return contactPersonRepository.findByEmailAddress(emailAddress); }

    public ContactPerson save(ContactPerson contactPerson) {
        return contactPersonRepository.save(contactPerson);
    }

    public boolean deleteById(UUID id) {
        if (contactPersonRepository.existsById(id)) {
            contactPersonRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public ContactPerson createContactPerson(ContactPersonCreateRequest request) {
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setContactNumber(request.contactNumber());
        contactPerson.setEmailAddress(request.emailAddress().orElse(null));

        return contactPersonRepository.save(contactPerson);
    }

    public ContactPerson updateContactPerson(UUID contactPersonId, ContactPersonCreateRequest request) {
        ContactPerson contactPerson = contactPersonRepository.findById(contactPersonId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find contact person for given id: " + contactPersonId));
        contactPerson.setContactNumber(request.contactNumber());
        contactPerson.setEmailAddress(request.emailAddress().orElse(null));
        return contactPersonRepository.save(contactPerson);
    }
}

