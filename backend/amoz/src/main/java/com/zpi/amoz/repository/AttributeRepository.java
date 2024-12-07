package com.zpi.amoz.repository;

import com.zpi.amoz.models.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    @Query("SELECT a FROM Attribute a " +
            "JOIN ProductAttribute pa ON pa.attribute = a " +
            "JOIN Product p ON pa.product = p " +
            "WHERE p.company.companyId = :companyId")
    List<Attribute> fetchAllProductAttributesByCompanyId(@Param("companyId") UUID companyId);

    @Query("SELECT a FROM Attribute a " +
            "JOIN VariantAttribute va ON va.attribute = a " +
            "JOIN ProductVariant pv ON va.productVariant = pv " +
            "JOIN Product p ON pv.product = p " +
            "WHERE p.company.companyId = :companyId")
    List<Attribute> fetchAllVariantAttributesByCompanyId(@Param("companyId") UUID companyId);

    @Query(value = "SELECT a.* FROM Attribute a " +
            "JOIN VariantAttribute va ON va.AttributeName = a.AttributeName " +
            "JOIN ProductVariant pv ON va.ProductVariantID = pv.ProductVariantID " +
            "JOIN Product p ON pv.ProductID = p.ProductID " +
            "WHERE p.CompanyID = :companyId " +
            "UNION " +
            "SELECT a.* FROM Attribute a " +
            "JOIN ProductAttribute pa ON pa.AttributeName = a.AttributeName " +
            "JOIN Product p ON pa.ProductID = p.ProductID " +
            "WHERE p.companyId = :companyId", nativeQuery = true
    )
    List<Attribute> fetchAllAttributesByCompanyId(@Param("companyId") String companyId);

    @Query("SELECT a FROM Attribute a WHERE a.attributeName = :attributeName")
    Optional<Attribute> findAttributeByName(@Param("attributeName") String attributeName);
}
