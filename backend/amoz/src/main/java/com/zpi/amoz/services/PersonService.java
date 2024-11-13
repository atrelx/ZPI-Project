package com.zpi.amoz.services;

import com.zpi.amoz.models.Person;
import com.zpi.amoz.repository.PersonRepository;
import com.zpi.amoz.requests.PersonCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(UUID id) {
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public boolean deleteById(UUID id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Person createPerson(PersonCreateRequest request) {
        Person person = new Person();
        person.setName(request.name());
        person.setSurname(request.surname());
        person.setDateOfBirth(request.dateOfBirth());
        person.setSex(request.sex());

        return personRepository.save(person);
    }

    public Person updatePerson(UUID personId, PersonCreateRequest request) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find person for given id: " + personId));
        person.setName(request.name());
        person.setSurname(request.surname());
        person.setDateOfBirth(request.dateOfBirth());
        person.setSex(request.sex());

        return personRepository.save(person);
    }
}
