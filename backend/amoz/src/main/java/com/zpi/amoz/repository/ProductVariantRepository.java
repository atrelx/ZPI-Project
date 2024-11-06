package com.zpi.amoz.repository;

import com.zpi.amoz.models.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.isActive = false WHERE pv.productVariantId = :productVariantId")
    int deactivateProductVariant(UUID productVariantId);
}

