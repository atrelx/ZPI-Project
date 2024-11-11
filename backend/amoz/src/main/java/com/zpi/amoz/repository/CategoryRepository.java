package com.zpi.amoz.repository;

import com.zpi.amoz.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query(value = "WITH RECURSIVE CategoryHierarchy AS ("
            + "SELECT A2.* FROM Product A1 "
            + "INNER JOIN Category A2 ON A1.CategoryID = A2.CategoryID "
            + "WHERE A1.CompanyID = :companyId "
            + "UNION ALL "
            + "SELECT A2.* FROM Category A2 "
            + "INNER JOIN CategoryHierarchy A3 ON A2.CategoryID = A3.ParentCategoryID"
            + ") "
            + "SELECT * FROM CategoryHierarchy", nativeQuery = true)
    List<Category> getCategoriesWithParentHierarchy(@Param("companyId") String companyId);

    @Query(value = "SELECT CASE WHEN COUNT(A1) > 0 THEN true ELSE false END FROM Category A1 WHERE A1.parentCategory.categoryId = :categoryId")
    boolean hasChild(@Param("categoryId") UUID categoryId);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.category.categoryId = :categoryId")
    Boolean isDirectProductCategory(@Param("categoryId") UUID categoryId);
}
