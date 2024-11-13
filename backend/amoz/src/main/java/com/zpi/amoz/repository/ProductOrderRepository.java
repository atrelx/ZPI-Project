package com.zpi.amoz.repository;

import com.zpi.amoz.models.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, UUID> {




    @Query("SELECT po FROM ProductOrder po " +
            "INNER JOIN ProductOrderItem poi ON po.productOrderId = poi.productOrder.productOrderId " +
            "INNER JOIN ProductVariant pv ON poi.productVariant.productVariantId = pv.productVariantId " +
            "INNER JOIN Product p ON p.productId = pv.product.productId " +
            "WHERE p.company.companyId = :companyId")
    List<ProductOrder> findByCompanyId(@Param("companyId") UUID companyId);
}
