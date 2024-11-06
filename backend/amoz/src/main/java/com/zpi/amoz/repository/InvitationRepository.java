package com.zpi.amoz.repository;

import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    Optional<Invitation> findByToken(UUID companyId);
}
