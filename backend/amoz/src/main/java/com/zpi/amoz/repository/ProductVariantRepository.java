package com.zpi.amoz.repository;

import com.zpi.amoz.models.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product.productId = :productId")
    List<ProductVariant> findAllByProductId(@Param("productId") UUID productId);

    @Query("SELECT p FROM ProductVariant p WHERE p.productVariantId = :productVariantId")
    Optional<ProductVariant> findByProductVariantId(@Param("productVariantId") UUID productVariantId);
}

