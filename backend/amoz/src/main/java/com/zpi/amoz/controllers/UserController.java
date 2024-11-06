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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal, @Valid @RequestBody UserRegisterRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        String sub = userPrincipal.getSub();

        try {
            userService.registerUser(sub, request);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/isRegistered")
    public ResponseEntity<UserIsRegisteredResponse> isUserRegistered(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        boolean isRegistered = userService.isUserRegistered(userPrincipal.getSub());
        UserIsRegisteredResponse response = new UserIsRegisteredResponse(isRegistered);
        return ResponseEntity.ok(response);
    }
}
