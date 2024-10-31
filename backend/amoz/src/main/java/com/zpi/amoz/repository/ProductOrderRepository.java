package com.zpi.amoz.repository;

import com.zpi.amoz.models.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, UUID> {
}
