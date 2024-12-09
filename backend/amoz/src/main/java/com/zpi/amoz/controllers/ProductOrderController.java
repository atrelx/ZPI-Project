package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.*;
import com.zpi.amoz.models.Invoice;
import com.zpi.amoz.models.ProductOrder;
import com.zpi.amoz.requests.ProductOrderCreateRequest;
import com.zpi.amoz.requests.ProductOrderItemCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.InvoiceService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productOrders")
@RequiredArgsConstructor
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private InvoiceService invoiceService;

    @Operation(summary = "Tworzenie nowego zamówienia", description = "Tworzy nowe zamówienie produktu.")
    @ApiResponse(responseCode = "201", description = "Zamówienie zostało pomyślnie utworzone",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductOrderDetailsDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "Błąd walidacji danych wejściowych",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do tworzenia zamówienia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono klienta lub produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping
    public ResponseEntity<?> createProductOrder(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody ProductOrderCreateRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (request.customerId().isPresent()) {
                if (!authorizationService.hasPermissionToAccessCustomer(userPrincipal, request.customerId().get())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given customer is not in your company"));
                }
            }
            for (ProductOrderItemCreateRequest productOrderItemCreateRequest : request.productOrderItems()) {
                UUID productVariantId = productOrderItemCreateRequest.productVariantId();
                if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given product variant ID is not in your company: " + productVariantId));
                }
            }
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();

            ProductOrder productOrder = productOrderService.createProductOrder(companyId, request);
            ProductOrderDetailsDTO productOrderDetailsDTO = ProductOrderDetailsDTO.toProductOrderDetailsDTO(productOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(productOrderDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Aktualizowanie zamówienia", description = "Aktualizuje istniejące zamówienie produktu.")
    @ApiResponse(responseCode = "200", description = "Zamówienie zostało pomyślnie zaktualizowane",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductOrderDetailsDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "Błąd walidacji danych wejściowych",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do edycji zamówienia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zamówienia lub klienta",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/{productOrderId}")
    public ResponseEntity<?> updateProductOrder(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productOrderId,
            @Valid @RequestBody ProductOrderCreateRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProductOrder(userPrincipal, productOrderId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product order is not in your company"));
            }
            if (request.customerId().isPresent()) {
                if (!authorizationService.hasPermissionToAccessCustomer(userPrincipal, request.customerId().get())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given customer is not in your company"));
                }
            }
            for (ProductOrderItemCreateRequest productOrderItemCreateRequest : request.productOrderItems()) {
                UUID productVariantId = productOrderItemCreateRequest.productVariantId();
                if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given product variant ID is not in your company: " + productVariantId));
                }
            }
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();

            ProductOrder productOrder = productOrderService.updateProductOrder(productOrderId, companyId, request);
            ProductOrderDetailsDTO productOrderDetailsDTO = ProductOrderDetailsDTO.toProductOrderDetailsDTO(productOrder);
            return ResponseEntity.status(HttpStatus.OK).body(productOrderDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Usuwanie zamówienia", description = "Usuwa istniejące zamówienie produktu.")
    @ApiResponse(responseCode = "200", description = "Zamówienie zostało pomyślnie usunięte",
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do usunięcia zamówienia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zamówienia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @DeleteMapping("/{productOrderId}")
    public ResponseEntity<?> deleteProductOrder(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productOrderId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProductOrder(userPrincipal, productOrderId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product order is not in your company"));
            }
            productOrderService.deleteProductOrder(productOrderId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Generowanie faktury dla zamówienia", description = "Generuje fakturę dla istniejącego zamówienia produktu.")
    @ApiResponse(responseCode = "200", description = "Faktura została pomyślnie wygenerowana",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceB2BDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do generowania faktury",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zamówienia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/{productOrderId}/generateInvoice")
    public ResponseEntity<?> generateInvoice(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productOrderId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProductOrder(userPrincipal, productOrderId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product order is not in your company"));
            }
            Invoice invoice = invoiceService.generateInvoice(productOrderId);
            return ResponseEntity.ok(InvoiceSummaryDTO.toInvoiceSummaryDTO(invoice));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching product details: " + e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz istniejącą fakturę", description = "Pobiera graficzną reprezenacje faktury w postaci PDF.")
    @ApiResponse(responseCode = "200", description = "Faktura została pomyślnie pobrana",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceB2BDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono faktury",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do pobrania faktury",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<?> downloadInvoice(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID invoiceId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageInvoice(userPrincipal, invoiceId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invoice is not in your company"));
            }
            Invoice invoice = invoiceService.findById(invoiceId)
                    .orElseThrow(() -> new EntityNotFoundException("Could not find invoice for given id: " + invoiceId));
            if (invoice.getProductOrder().getCustomer() == null) {
                throw new EntityNotFoundException("Customer is required to fetch an invoice");
            }
            byte[] invoicePDFBytes;
            if (invoice.getProductOrder().getCustomer().getCustomerB2B() != null) {
                invoicePDFBytes = invoiceService.generatePDFFromInvoiceB2B(InvoiceB2BDTO.toInvoiceB2BDTO(invoice));
            } else {
                invoicePDFBytes = invoiceService.generatePDFFromInvoiceB2C(InvoiceB2CDTO.toInvoiceB2CDTO(invoice));
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice_no." + invoice.getInvoiceNumber() + ".pdf");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(invoicePDFBytes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching product details: " + e.getMessage()));
        }
    }

    @Operation(summary = "Wyślij fakturę klientowi", description = "Wysyła emailowo (jeśli podany) graficzną reprezenacje faktury w postaci PDF.")
    @ApiResponse(responseCode = "200", description = "Faktura została pomyślnie wysłana",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceB2BDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono faktury",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do pobrania faktury",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping("/invoice/{invoiceId}")
    public ResponseEntity<?> sendInvoiceToCustomer(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID invoiceId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageInvoice(userPrincipal, invoiceId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invoice is not in your company"));
            }
            Invoice invoice = invoiceService.findById(invoiceId)
                    .orElseThrow(() -> new EntityNotFoundException("Could not find invoice for given id: " + invoiceId));
            if (invoice.getProductOrder().getCustomer() == null) {
                throw new EntityNotFoundException("Customer is required to fetch an invoice");
            }

            invoiceService.sendInvoiceToCustomer(invoice, userPrincipal.getSub());

            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching product details: " + e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz szczegóły zamówienia produktu", description = "Zwraca szczegóły konkretnego zamówienia produktu.")
    @ApiResponse(responseCode = "200", description = "Zamówienie zostało pomyślnie pobrane",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductOrderDetailsDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zamówienia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do wyświetlenia zamówienia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/{productOrderId}")
    public ResponseEntity<?> getProductOrderDetails(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productOrderId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProductOrder(userPrincipal, productOrderId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product order is not in your company"));
            }
            ProductOrder productOrder = productOrderService.findById(productOrderId)
                    .orElseThrow(() -> new EntityNotFoundException("Product order not found with id: " + productOrderId));
            ProductOrderDetailsDTO productOrderDetailsDTO = ProductOrderDetailsDTO.toProductOrderDetailsDTO(productOrder);

            return ResponseEntity.ok(productOrderDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching product details: " + e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz wszystkie zamówienia produktu", description = "Zwraca listę wszystkich zamówień w danej firmie.")
    @ApiResponse(responseCode = "200", description = "Lista zamówień została pomyślnie pobrana",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductOrderSummaryDTO.class)))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zamówień",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping
    public ResponseEntity<?> getAllProductOrderSummary(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("You are not in any company"))
                    .getCompanyId();
            List<ProductOrder> productOrders = productOrderService.findByCompanyId(companyId);

            List<ProductOrderSummaryDTO> productOrderSummaryDTOs = productOrders.stream()
                    .map(ProductOrderSummaryDTO::toProductOrderSummaryDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(productOrderSummaryDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching product details: " + e.getMessage()));
        }
    }
}

