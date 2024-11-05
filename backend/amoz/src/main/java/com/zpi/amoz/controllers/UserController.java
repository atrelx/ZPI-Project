package com.zpi.amoz.controllers;

import com.zpi.amoz.enums.SystemRole;
import com.zpi.amoz.models.ContactPerson;
import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.Person;
import com.zpi.amoz.models.User;
import com.zpi.amoz.requests.UserRegisterRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.responses.UserIsRegisteredResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.ContactPersonService;
import com.zpi.amoz.services.EmployeeService;
import com.zpi.amoz.services.PersonService;
import com.zpi.amoz.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ContactPersonService contactPersonService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal, @Valid @RequestBody UserRegisterRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        String sub = userPrincipal.getSub();

        if (userService.findById(sub).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("User already exists"));
        }

        User user = new User();
        user.setUserId(sub);
        user.setSystemRole(SystemRole.USER);

        Person person = new Person();
        person.setName(request.name());
        person.setSurname(request.surname());
        person.setDateOfBirth(request.dateOfBirth());
        person.setSex(request.sex());

        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setContactNumber(request.contactNumber());
        contactPerson.setEmailAddress(request.emailAddress().orElse(null));

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setContactPerson(contactPerson);
        employee.setPerson(person);

        userService.save(user);
        personService.save(person);
        contactPersonService.save(contactPerson);
        employeeService.save(employee);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/isRegistered")
    public ResponseEntity<UserIsRegisteredResponse> isUserRegistered(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        boolean isRegistered = userService.findById(userPrincipal.getSub()).isPresent();
        UserIsRegisteredResponse response = new UserIsRegisteredResponse(isRegistered);
        return ResponseEntity.ok(response);
    }
}
