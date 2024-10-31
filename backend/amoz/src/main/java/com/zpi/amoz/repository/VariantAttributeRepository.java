package com.zpi.amoz.repository;

import com.zpi.amoz.models.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, UUID> {
}

