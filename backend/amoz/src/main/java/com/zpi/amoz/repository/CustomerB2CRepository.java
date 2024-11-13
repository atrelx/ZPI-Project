package com.zpi.amoz.repository;


import com.zpi.amoz.models.CustomerB2B;
import com.zpi.amoz.models.CustomerB2C;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerB2CRepository extends JpaRepository<CustomerB2C, UUID> {
    @Query("SELECT cb2c from CustomerB2C cb2c WHERE cb2c.customerId.customer.customerId = :customerId")
    Optional<CustomerB2C> findByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT cb2c from CustomerB2C cb2c WHERE cb2c.customerId.customer.company.companyId = :companyId")
    List<CustomerB2C> findAllByCompanyId(@Param("companyId") UUID companyId);
}

