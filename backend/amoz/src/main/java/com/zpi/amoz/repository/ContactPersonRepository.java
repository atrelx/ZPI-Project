package com.zpi.amoz.repository;

import com.zpi.amoz.models.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, UUID> {
}

