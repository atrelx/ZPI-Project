package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.CustomerB2BDTO;
import com.zpi.amoz.dtos.CustomerB2CDTO;
import com.zpi.amoz.dtos.ProductOrderDetailsDTO;
import com.zpi.amoz.dtos.ProductOrderSummaryDTO;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.CustomerB2B;
import com.zpi.amoz.models.CustomerB2C;
import com.zpi.amoz.models.ProductOrder;
import com.zpi.amoz.requests.CustomerB2BCreateRequest;
import com.zpi.amoz.requests.CustomerB2CCreateRequest;
import com.zpi.amoz.requests.ProductOrderCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.CustomerService;
import com.zpi.amoz.services.ProductOrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/b2b")
    public ResponseEntity<?> createCustomerB2B(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody CustomerB2BCreateRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            CustomerB2B customerB2B = customerService.createCustomerB2B(userPrincipal.getSub(), request);
            CustomerB2BDTO customerB2BDTO = CustomerB2BDTO.toCustomerB2BDTO(customerB2B);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerB2BDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @PostMapping("/b2c")
    public ResponseEntity<?> createCustomerB2C(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                               @Valid @RequestBody CustomerB2CCreateRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            CustomerB2C customerB2C = customerService.createCustomerB2C(userPrincipal.getSub(), request);
            CustomerB2CDTO customerB2CDTO = CustomerB2CDTO.toCustomerB2CDTO(customerB2C);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerB2CDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @PutMapping("/b2b/{customerId}")
    public ResponseEntity<?> updateCustomerB2B(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                               @Valid @RequestBody CustomerB2BCreateRequest request,
                                               @PathVariable UUID customerId) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            CustomerB2B customerB2B = customerService.updateCustomerB2B(customerId, request);
            CustomerB2BDTO customerB2BDTO = CustomerB2BDTO.toCustomerB2BDTO(customerB2B);
            return ResponseEntity.status(HttpStatus.OK).body(customerB2BDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @PutMapping("/b2c/{customerId}")
    public ResponseEntity<?> updateCustomerB2C(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                               @Valid @RequestBody CustomerB2CCreateRequest request,
                                               @PathVariable UUID customerId) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            CustomerB2C customerB2C = customerService.updateCustomerB2C(customerId, request);
            CustomerB2CDTO customerB2CDTO = CustomerB2CDTO.toCustomerB2CDTO(customerB2C);
            return ResponseEntity.status(HttpStatus.OK).body(customerB2CDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @GetMapping("/b2b")
    public ResponseEntity<?> getAllCustomersB2B(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            List<CustomerB2B> customerB2BList = customerService.findAllCustomersB2BBySub(userPrincipal.getSub());
            List<CustomerB2BDTO> customerB2BDTOs = customerB2BList.stream().map(CustomerB2BDTO::toCustomerB2BDTO).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(customerB2BDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @GetMapping("/b2c")
    public ResponseEntity<?> getAllCustomersB2C(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            List<CustomerB2C> customerB2CList = customerService.findAllCustomersB2CBySub(userPrincipal.getSub());
            List<CustomerB2CDTO> customerB2CDTOs = customerB2CList.stream().map(CustomerB2CDTO::toCustomerB2CDTO).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(customerB2CDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }
}
