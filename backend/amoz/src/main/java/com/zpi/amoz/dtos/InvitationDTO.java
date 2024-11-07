package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Invitation;

import java.util.UUID;

public record InvitationDTO(
        UUID invitationId,
        UUID companyId,
        String employeeEmail,
        UUID token
) {
    public static InvitationDTO toInvitationDTO(Invitation invitation) {
        return new InvitationDTO(
                invitation.getInvitationId(),
                invitation.getCompany() != null ? invitation.getCompany().getCompanyId() : null,
                invitation.getEmployeeEmail(),
                invitation.getToken()
        );
    }
}
