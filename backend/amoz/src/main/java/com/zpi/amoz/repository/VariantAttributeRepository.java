package com.zpi.amoz.repository;

import com.zpi.amoz.models.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM VariantAttribute va WHERE va.productVariant.productVariantId = :productVariantId")
    void deleteAllByProductVariantId(@Param("productVariantId") UUID productVariantId);
}

