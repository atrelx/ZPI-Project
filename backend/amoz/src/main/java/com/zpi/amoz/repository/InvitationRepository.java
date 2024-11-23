package com.zpi.amoz.repository;

import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    Optional<Invitation> findByToken(UUID companyId);

    @Query("SELECT i FROM Invitation i JOIN Employee e ON i.employee = e WHERE e.user.userId = :userId")
    List<Invitation> findByUserId(String userId);
}
