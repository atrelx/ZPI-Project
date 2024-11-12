package com.zpi.amoz.repository;

import com.zpi.amoz.models.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductAttribute pa WHERE pa.product.productId = :productId")
    void deleteAllByProductId(@Param("productId") UUID productId);
}

