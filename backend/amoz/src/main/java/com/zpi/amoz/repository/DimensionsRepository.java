package com.zpi.amoz.repository;

import com.zpi.amoz.models.Dimensions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DimensionsRepository extends JpaRepository<Dimensions, UUID> {
}
