package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Invitation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

@Schema(description = "DTO reprezentujące zaproszenie, zawierające informacje o zaproszeniu do firmy, pracowniku oraz unikalnym tokenie.")
public record InvitationDTO(
        @Schema(description = "Szczegóły firmy od której wysłane jest zaproszenie do dołączenia do niej", implementation = CompanyDTO.class)
        CompanyDTO company,

        @Schema(description = "Szczegóły osoby wysyłającej zaproszenie", implementation = EmployeeDTO.class)
        EmployeeDTO sender,

        @Schema(description = "Unikalny token zaproszenia, używany do autentykacji i akceptacji zaproszenia", example = "a3e8c897-f2e6-4c42-9d0a-58f1a928b3a1")
        UUID token
) {
    public static InvitationDTO toInvitationDTO(Invitation invitation) {
        return new InvitationDTO(
                CompanyDTO.toCompanyDTO(invitation.getCompany()),
                EmployeeDTO.toEmployeeDTO(invitation.getCompany().getEmployees().stream()
                        .filter(employee -> employee.getRoleInCompany() == RoleInCompany.OWNER)
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Could not find owner for given company"))),
                invitation.getToken()
        );
    }
}


