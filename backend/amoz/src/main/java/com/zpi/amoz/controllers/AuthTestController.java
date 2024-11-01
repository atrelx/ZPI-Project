package com.zpi.amoz.controllers;

import com.zpi.amoz.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth-test")
public class AuthTestController {
    @GetMapping("/me")
    public ResponseEntity<UserPrincipal> fetchMyPersonalData(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);

        return ResponseEntity.ok(userPrincipal);
    }
}
