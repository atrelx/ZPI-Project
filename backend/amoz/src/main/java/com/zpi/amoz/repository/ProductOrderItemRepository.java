package com.zpi.amoz.repository;


import com.zpi.amoz.models.ProductOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductOrderItemRepository extends JpaRepository<ProductOrderItem, UUID> {
}

