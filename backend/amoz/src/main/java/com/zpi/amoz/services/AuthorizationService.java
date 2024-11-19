package com.zpi.amoz.services;

import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AuthorizationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvitationRepository invitationRepository;


    private UUID getCompanyIdForUserPrincipal(UserPrincipal userPrincipal) {
        return companyService.getCompanyByUserId(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("User is not in any company"))
                .getCompanyId();
    }

    public boolean hasPermissionToManageCompany(UserPrincipal userPrincipal, UUID companyId) {
        Employee employee = employeeRepository.findByUser_UserId(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("Could not find Employee for given user id: " + userPrincipal.getSub()));

        return employee.getCompany().getCompanyId().equals(companyId) && employee.getRoleInCompany() == RoleInCompany.OWNER;
    }

    public boolean hasPermissionToReadCompany(UserPrincipal userPrincipal, UUID companyId) {
        Employee employee = employeeRepository.findByUser_UserId(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("Could not find Employee for given user id: " + userPrincipal.getSub()));

        return employee.getCompany().getCompanyId().equals(companyId) && employee.getRoleInCompany() == RoleInCompany.OWNER;
    }


    public boolean hasPermissionToManageProduct(UserPrincipal userPrincipal, UUID productId) {
        UUID userCompanyId = getCompanyIdForUserPrincipal(userPrincipal);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return product.getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToManageProductVariant(UserPrincipal userPrincipal, UUID productVariantId) {
        UUID userCompanyId = getCompanyIdForUserPrincipal(userPrincipal);
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found"));
        return productVariant.getProduct().getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToManageCategory(UserPrincipal userPrincipal, UUID categoryId) {
        UUID userCompanyId = getCompanyIdForUserPrincipal(userPrincipal);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return category.getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToManageProductOrder(UserPrincipal userPrincipal, UUID productOrderId) {
        UUID userCompanyId = getCompanyIdForUserPrincipal(userPrincipal);
        ProductOrder productOrder = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Product order not found"));
        return productOrder.getOrderItems().get(0).getProductVariant().getProduct().getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToAccessCustomer(UserPrincipal userPrincipal, UUID customerId) {
        UUID userCompanyId = getCompanyIdForUserPrincipal(userPrincipal);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return customer.getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToManageEmployee(UserPrincipal userPrincipal, UUID employeeId) {
        UUID userCompanyId = getCompanyIdForUserPrincipal(userPrincipal);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employee.getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToManageInvoice(UserPrincipal userPrincipal, UUID invoiceId) {
        UUID userCompanyId = getCompanyIdForUserPrincipal(userPrincipal);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
        return invoice.getProductOrder().getOrderItems().get(0).getProductVariant().getProduct().getCompany().getCompanyId().equals(userCompanyId);
    }

    public boolean hasPermissionToManageInvitation(UserPrincipal userPrincipal, UUID invitationId) {
        UUID userCompanyId = getCompanyIdForUserPrincipal(userPrincipal);
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));
        return invitation.getCompany().getCompanyId().equals(userCompanyId);
    }


}
