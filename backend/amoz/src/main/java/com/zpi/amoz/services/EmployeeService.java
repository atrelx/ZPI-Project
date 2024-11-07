package com.zpi.amoz.services;

import com.zpi.amoz.dtos.EmployeeDTO;
import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.ContactPerson;
import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.Invitation;
import com.zpi.amoz.repository.CompanyRepository;
import com.zpi.amoz.repository.ContactPersonRepository;
import com.zpi.amoz.repository.EmployeeRepository;
import com.zpi.amoz.repository.InvitationRepository;
import com.zpi.amoz.responses.EmployeeDetailsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Autowired
    private EmailService emailService;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public void kickFromCompanyById(UUID companyId, UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot found employee by ID: " + employeeId));

        if (companyId.equals(employee.getCompany().getCompanyId())) {
            employee.setCompany(null);
            employeeRepository.save(employee);
        } else {
            throw new IllegalArgumentException("Employee does not belong to specified company.");
        }
    }

    @Transactional
    public CompletableFuture<Void> inviteEmployeeToCompany(UUID companyId, String employeeEmailAddress) {
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
        }
        return CompletableFuture.completedFuture(null);
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
        employee.setEmploymentDate(LocalDate.now());
        employeeRepository.save(employee);
        invitationRepository.deleteById(invitation.getInvitationId());
    }

    public List<EmployeeDTO> getEmployeesByCompanyId(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + companyId));

        List<Employee> employees = employeeRepository.findAllByCompany(company);

        return employees.stream()
                .map(EmployeeDTO::toEmployeeDTO)
                .collect(Collectors.toList());
    }
}
