package com.zpi.amoz.services;

import com.zpi.amoz.dtos.CompanyDTO;
import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanyService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Autowired
    private InvitationRepository invitationRepository;


    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(UUID id) {
        return companyRepository.findById(id);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public Company createCompany(String userId, CompanyDTO companyDetails) {
        Company company = new Company();
        company.setCompanyNumber(companyDetails.companyNumber());
        company.setCountryOfRegistration(companyDetails.countryOfRegistration());
        company.setName(companyDetails.name());

        companyDetails.regon().ifPresent(company::setRegon);

        Address companyAddress = new Address();
        companyAddress.setCity(companyDetails.address().city());
        companyAddress.setStreet(companyDetails.address().street());
        companyAddress.setStreetNumber(companyDetails.address().streetNumber());
        companyAddress.setApartmentNumber(companyDetails.address().apartmentNumber());
        companyAddress.setPostalCode(companyDetails.address().postalCode());

        companyDetails.address().additionalInformation().ifPresent(companyAddress::setAdditionalInformation);

        Address savedAddress = addressRepository.save(companyAddress);
        company.setAddress(savedAddress);

        Company savedCompany = companyRepository.save(company);

        Employee employee = employeeRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Employee not found with UserId: " + userId));

        employee.setCompany(savedCompany);
        employee.setRoleInCompany(RoleInCompany.OWNER);
        employeeRepository.save(employee);

        return savedCompany;
    }

    public Optional<Company> update(UUID id, CompanyDTO companyDetails) {
        return companyRepository.findById(id).map(company -> {
            company.setCompanyNumber(companyDetails.companyNumber());
            company.setCountryOfRegistration(companyDetails.countryOfRegistration());
            company.setName(companyDetails.name());
            companyDetails.regon().ifPresentOrElse(company::setRegon, () -> company.setRegon(null));

            Address companyAddress = company.getAddress();
            companyAddress.setCity(companyDetails.address().city());
            companyAddress.setStreet(companyDetails.address().street());
            companyAddress.setStreetNumber(companyDetails.address().streetNumber());
            companyAddress.setApartmentNumber(companyDetails.address().apartmentNumber());
            companyAddress.setPostalCode(companyDetails.address().postalCode());
            companyDetails.address().additionalInformation()
                    .ifPresentOrElse(companyAddress::setAdditionalInformation, () -> companyAddress.setAdditionalInformation(null));

            addressRepository.save(companyAddress);
            return companyRepository.save(company);
        });
    }

    public boolean deactivateCompanyById(UUID id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();

            for (Employee employee : company.getEmployees()) {
                employee.setCompany(null);
                employee.setRoleInCompany(null);
                employeeRepository.save(employee);
            }

            companyRepository.deactivateCompany(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public CompletableFuture<Boolean> inviteEmployeeToCompany(UUID companyId, String employeeEmailAddress) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        ContactPerson contactPerson = contactPersonRepository.findByEmailAddress(employeeEmailAddress)
                .orElseThrow(() -> new EntityNotFoundException("Contact person not found"));

        Employee employee = Optional.ofNullable(contactPerson.getEmployee())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (employee.getCompany() != null) {
            throw new RuntimeException("Employee already has company");
        }

        Invitation invitation = new Invitation();

        invitation.setCompany(company);
        invitation.setEmployeeEmail(employeeEmailAddress);
        invitationRepository.save(invitation);

        UUID confirmationToken = invitation.getToken();

        String subject = "Zostałeś zaproszony do firmy " + company.getName() + " w aplikacji AMOZ.";
        String htmlContent = "<html><body>" +
                "<h2>Witaj " + employee.getPerson().getName() + "!</h2>" +
                "<p>Zostałeś zaproszony do firmy <b>" + company.getName() + "</b> w aplikacji AMOZ.</p>" +
                "<p>Proszę kliknij poniższy link, aby przyjąć zaproszenie:</p>" +
                "<a href='amoz://acceptInvitation?token=" + confirmationToken.toString() + "'>Przyjmij zaproszenie</a>" +
                "</body></html>";

        CompletableFuture<Boolean> emailResult = emailService.sendEmail(Collections.singletonList(employeeEmailAddress), subject, htmlContent);

        boolean emailSentSuccessfully = emailResult.join();

        if (!emailSentSuccessfully) {
            throw new RuntimeException("Email sending failed.");
        } else {
            return CompletableFuture.completedFuture(true);
        }
    }

    @Transactional
    public void acceptInvitationToCompany(UUID confirmationToken) {
        Invitation invitation = invitationRepository.findByToken(confirmationToken)
                .orElseThrow(() -> new EntityNotFoundException("Invalid confirmation token"));

        ContactPerson contactPerson = contactPersonRepository.findByEmailAddress(invitation.getEmployeeEmail())
                .orElseThrow(() -> new EntityNotFoundException("Contact person not found"));

        Employee employee = Optional.ofNullable(contactPerson.getEmployee())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (employee.getCompany() != null) {
            throw new RuntimeException("Employee already has company");
        }

        employee.setCompany(invitation.getCompany());
        employee.setRoleInCompany(RoleInCompany.REGULAR);
        employeeRepository.save(employee);
        
        invitationRepository.deleteById(invitation.getInvitationId());
    }
}

