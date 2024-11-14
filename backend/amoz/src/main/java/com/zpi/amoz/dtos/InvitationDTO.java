package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Invitation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO reprezentujące zaproszenie, zawierające informacje o zaproszeniu do firmy, pracowniku oraz unikalnym tokenie.")
public record InvitationDTO(

        @Schema(description = "Identyfikator zaproszenia", example = "e4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ed")
        UUID invitationId,

        @Schema(description = "Identyfikator firmy, do której należy zaproszenie", example = "8d49c987-37c4-43b3-ae5e-59c47c89b279")
        UUID companyId,

        @Schema(description = "Identyfikator pracownika, który otrzymał zaproszenie", example = "f64f3870-b9fa-4b6b-b1ed-7df2e78b9d92")
        UUID employeeId,

        @Schema(description = "Unikalny token zaproszenia, używany do autentykacji i akceptacji zaproszenia", example = "a3e8c897-f2e6-4c42-9d0a-58f1a928b3a1")
        UUID token
) {
    public static InvitationDTO toInvitationDTO(Invitation invitation) {
        return new InvitationDTO(
                invitation.getInvitationId(),
                invitation.getCompany().getCompanyId(),
                invitation.getEmployee().getEmployeeId(),
                invitation.getToken()
        );
    }
}


