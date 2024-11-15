package com.zpi.amoz.repository;

import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isActive = false WHERE p.productId = :productId")
    int deactivateProduct(UUID productId);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.company.companyId = :companyId")
    List<Product> findAllByCompanyId(UUID companyId);

    Optional<Product> findByCategory_CategoryId(UUID categoryId);
}

