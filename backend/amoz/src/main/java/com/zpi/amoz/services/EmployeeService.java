package com.zpi.amoz.services;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.zpi.amoz.dtos.EmployeeDTO;
import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import com.zpi.amoz.requests.PushRequest;
import com.zpi.amoz.requests.UserRegisterRequest;
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
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PushService pushService;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> findEmployeeByUserId(String userId) {
        return employeeRepository.findByUser_UserId(userId);
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
                .orElseThrow(() -> new EntityNotFoundException("Company for given id found: " + companyId));

        Employee employee = employeeRepository.findByContactPerson_EmailAddress(employeeEmailAddress)
                .orElseThrow(() -> new EntityNotFoundException("Employee for given email address not found: " + employeeEmailAddress));

        if (employee.getCompany() != null) {
            throw new RuntimeException("Employee already has company");
        }

        Invitation invitation = new Invitation();

        invitation.setCompany(company);
        invitation.setEmployee(employee);
        invitationRepository.save(invitation);

        UUID confirmationToken = invitation.getToken();

        String deeplink = "amoz://acceptinvitation?token=" + confirmationToken.toString();

        String subject = "Zostałeś zaproszony do firmy " + company.getName() + " w aplikacji AMOZ.";
        String htmlContent = "<html><body>" +
                "<h2>Witaj " + employee.getPerson().getName() + "!</h2>" +
                "<p>Zostałeś zaproszony do firmy <b>" + company.getName() + "</b> w aplikacji AMOZ.</p>" +
                "<p>Proszę kliknij poniższy link, aby przyjąć zaproszenie:</p>" +
                "<a href='" + deeplink + "'>Przyjmij zaproszenie</a>" +
                "</body></html>";

        CompletableFuture<Boolean> emailResult = emailService.sendEmail(Collections.singletonList(employeeEmailAddress), subject, htmlContent);

        boolean emailSentSuccessfully = emailResult.join();

        if (!emailSentSuccessfully) {
            throw new RuntimeException("Email sending failed.");
        } else {
            User user = employee.getUser();
            PushRequest request = new PushRequest("Zostałeś zaproszony do firmy " + company.getName(),
                    "Kliknij w poniższe powiadomienie by odpowiedzieć na zaproszenie w aplikacji AMOZ",
                    Optional.of(deeplink));
            try {
                pushService.sendMessage(user.getPushToken(), request);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException("Push sending failed");
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void acceptInvitationToCompany(UUID confirmationToken) {
        Invitation invitation = invitationRepository.findByToken(confirmationToken)
                .orElseThrow(() -> new EntityNotFoundException("Invalid confirmation token"));

        Employee employee = invitation.getEmployee();

        if (employee.getCompany() != null) {
            throw new RuntimeException("Employee already has company");
        }

        employee.setCompany(invitation.getCompany());
        employee.setRoleInCompany(RoleInCompany.REGULAR);
        employee.setEmploymentDate(LocalDate.now());
        employeeRepository.save(employee);
        invitationRepository.deleteById(invitation.getInvitationId());
    }

    @Transactional
    public void rejectInvitationToCompany(UUID confirmationToken) {
        Invitation invitation = invitationRepository.findByToken(confirmationToken)
                .orElseThrow(() -> new EntityNotFoundException("Invalid confirmation token"));

        invitationRepository.deleteById(invitation.getInvitationId());
    }

    public List<Invitation> fetchAllInvitations(String userId) {
        return invitationRepository.findByUserId(userId);
    }

    public List<Employee> getEmployeesByCompanyId(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + companyId));

        return employeeRepository.findAllByCompany(company);
    }


    @Transactional
    public Employee createEmployee(User user, ContactPerson contactPerson, Person person) {
        Employee employee = new Employee();
        employee.setUser(user);
        employee.setContactPerson(contactPerson);
        employee.setPerson(person);

        return employeeRepository.save(employee);
    }
}
