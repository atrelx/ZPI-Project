package com.zpi.amoz.repository;

import com.zpi.amoz.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c WHERE c.company.companyId = :companyId")
    List<Category> getCategoriesWithParentHierarchy(@Param("companyId") UUID companyId);

    @Query(value = "SELECT CASE WHEN COUNT(A1) > 0 THEN true ELSE false END FROM Category A1 WHERE A1.parentCategory.categoryId = :categoryId")
    boolean hasChild(@Param("categoryId") UUID categoryId);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.category.categoryId = :categoryId")
    Boolean isDirectProductCategory(@Param("categoryId") UUID categoryId);
}
