package com.zpi.amoz.services;

import com.zpi.amoz.enums.SystemRole;
import com.zpi.amoz.models.ContactPerson;
import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.Person;
import com.zpi.amoz.models.User;
import com.zpi.amoz.repository.UserRepository;
import com.zpi.amoz.requests.UserRegisterRequest;
import com.zpi.amoz.responses.MessageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private EmployeeService employeeRepository;
    @Autowired
    private PersonService personRepository;
    @Autowired
    private ContactPersonService contactPersonRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void registerUser(String sub, UserRegisterRequest request) {
        if (findById(sub).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUserId(sub);
        user.setSystemRole(SystemRole.USER);

        User savedUser = userRepository.save(user);

        Person person = new Person();
        person.setName(request.name());
        person.setSurname(request.surname());
        person.setDateOfBirth(request.dateOfBirth());
        person.setSex(request.sex());

        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setContactNumber(request.contactNumber());
        contactPerson.setEmailAddress(request.emailAddress().orElse(null));

        Employee employee = new Employee();
        employee.setUser(savedUser);
        employee.setContactPerson(contactPerson);
        employee.setPerson(person);

        personRepository.save(person);
        contactPersonRepository.save(contactPerson);
        employeeRepository.save(employee);
    }

    public boolean isUserRegistered(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean deleteById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
