package com.zpi.amoz.repository;

import com.zpi.amoz.models.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    @Query("SELECT a FROM Attribute a " +
            "JOIN ProductAttribute pa ON pa.attribute = a " +
            "JOIN Product p ON pa.product = p " +
            "WHERE p.company.companyId = :companyId")
    List<Attribute> fetchAllAttributesByCompanyId(@Param("companyId") UUID companyId);


}
