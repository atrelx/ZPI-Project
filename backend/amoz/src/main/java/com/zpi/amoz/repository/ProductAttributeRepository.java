package com.zpi.amoz.repository;

import com.zpi.amoz.models.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, UUID> {
}

