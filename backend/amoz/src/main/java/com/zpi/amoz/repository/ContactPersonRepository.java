package com.zpi.amoz.repository;

import com.zpi.amoz.models.ContactPerson;
import com.zpi.amoz.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, UUID> {
    Optional<ContactPerson> findByEmailAddress(String userId);
}

