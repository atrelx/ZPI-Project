package com.zpi.amoz.repository;

import com.zpi.amoz.models.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WeightRepository extends JpaRepository<Weight, UUID> {
}

