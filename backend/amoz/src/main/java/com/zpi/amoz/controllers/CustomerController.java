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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Pobierz klienta na podstawie typu", description = "Zwraca klienta B2B lub B2C na podstawie przekazanego typu klienta.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano klienta B2B",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerB2BDTO.class))
    )
    @ApiResponse(responseCode = "202", description = "Pomyślnie pobrano klienta B2C",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerB2CDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono klienta",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerDetails(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable("customerId") UUID customerId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToAccessCustomer(userPrincipal, customerId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given customer is not in your company"));
            }
            if (customerService.isCustomerB2B(customerId)) {
                List<CustomerB2B> customerB2BList = customerService.findAllCustomersB2BBySub(userPrincipal.getSub());
                List<CustomerB2BDTO> customerB2BDTOs = customerB2BList.stream()
                        .map(CustomerB2BDTO::toCustomerB2BDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.OK).body(customerB2BDTOs);
            } else {
                List<CustomerB2C> customerB2CList = customerService.findAllCustomersB2CBySub(userPrincipal.getSub());
                List<CustomerB2CDTO> customerB2CDTOs = customerB2CList.stream()
                        .map(CustomerB2CDTO::toCustomerB2CDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerB2CDTOs);
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Utwórz klienta B2B", description = "Tworzy nowego klienta B2B na podstawie danych przekazanych w żądaniu.")
    @ApiResponse(responseCode = "201", description = "Klient B2B został pomyślnie utworzony",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerB2BDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zasobu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping("/b2b")
    public ResponseEntity<?> createCustomerB2B(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody CustomerB2BCreateRequest request
    ) {
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

    @Operation(summary = "Utwórz klienta B2C", description = "Tworzy nowego klienta B2C na podstawie danych przekazanych w żądaniu.")
    @ApiResponse(responseCode = "201", description = "Klient B2C został pomyślnie utworzony",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerB2CDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zasobu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping("/b2c")
    public ResponseEntity<?> createCustomerB2C(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody CustomerB2CCreateRequest request
    ) {
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

    @Operation(summary = "Aktualizuj klienta B2B", description = "Aktualizuje dane klienta B2B na podstawie przekazanych danych.")
    @ApiResponse(responseCode = "200", description = "Klient B2B został pomyślnie zaktualizowany",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerB2BDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do edytowania klienta",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono klienta",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/b2b/{customerId}")
    public ResponseEntity<?> updateCustomerB2B(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody CustomerB2BCreateRequest request,
            @PathVariable UUID customerId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToAccessCustomer(userPrincipal, customerId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given customer is not in your company"));
            }
            CustomerB2B customerB2B = customerService.updateCustomerB2B(customerId, request);
            CustomerB2BDTO customerB2BDTO = CustomerB2BDTO.toCustomerB2BDTO(customerB2B);
            return ResponseEntity.status(HttpStatus.OK).body(customerB2BDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Aktualizuj klienta B2C", description = "Aktualizuje dane klienta B2C na podstawie przekazanych danych.")
    @ApiResponse(responseCode = "200", description = "Klient B2C został pomyślnie zaktualizowany",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerB2CDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do edytowania klienta",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono klienta",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/b2c/{customerId}")
    public ResponseEntity<?> updateCustomerB2C(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody CustomerB2CCreateRequest request,
            @PathVariable UUID customerId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToAccessCustomer(userPrincipal, customerId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given customer is not in your company"));
            }
            CustomerB2C customerB2C = customerService.updateCustomerB2C(customerId, request);
            CustomerB2CDTO customerB2CDTO = CustomerB2CDTO.toCustomerB2CDTO(customerB2C);
            return ResponseEntity.status(HttpStatus.OK).body(customerB2CDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Pobierz wszystkich klientów B2B", description = "Zwraca listę wszystkich klientów B2B przypisanych do firmy.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano klientów B2B",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerB2BDTO.class)))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono klientów",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/b2b")
    public ResponseEntity<?> getAllCustomersB2B(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
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

    @Operation(summary = "Pobierz wszystkich klientów B2C", description = "Zwraca listę wszystkich klientów B2C przypisanych do firmy.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano klientów B2C",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerB2CDTO.class)))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono klientów",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/b2c")
    public ResponseEntity<?> getAllCustomersB2C(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
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
