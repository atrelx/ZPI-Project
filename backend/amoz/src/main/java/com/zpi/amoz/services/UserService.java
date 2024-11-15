package com.zpi.amoz.services;

import com.zpi.amoz.enums.SystemRole;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.UsersRepository;
import com.zpi.amoz.requests.UserRegisterRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    private final UsersRepository usersRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ContactPersonService contactPersonService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return usersRepository.findById(id);
    }

    @Transactional
    public User registerUser(String sub, UserRegisterRequest request) {
        if (findById(sub).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = this.createUser(sub, SystemRole.USER);
        Person person = personService.createPerson(request.person());
        ContactPerson contactPerson = contactPersonService.createContactPerson(request.contactPerson());
        Employee employee = employeeService.createEmployee(user, contactPerson, person);

        return user;
    }

    @Transactional
    public User updateUser(String sub, UserRegisterRequest request) {
        User user = findById(sub)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UUID personId = user.getEmployee().getPerson().getPersonId();
        personService.updatePerson(personId, request.person());

        UUID contactPersonId = user.getEmployee().getContactPerson().getContactPersonId();
        contactPersonService.updateContactPerson(contactPersonId, request.contactPerson());

        return user;
    }

    public User createUser(String sub, SystemRole systemRole) {
        User user = new User();
        user.setUserId(sub);
        user.setSystemRole(systemRole);
        return usersRepository.save(user);
    }

    public boolean isUserRegistered(String userId) {
        return usersRepository.findById(userId).isPresent();
    }

    public User save(User user) {
        return usersRepository.save(user);
    }

    public boolean deleteById(String id) {
        if (usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
