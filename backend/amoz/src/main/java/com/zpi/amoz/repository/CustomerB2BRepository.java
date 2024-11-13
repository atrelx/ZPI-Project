package com.zpi.amoz.repository;

import com.zpi.amoz.models.CustomerB2B;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerB2BRepository extends JpaRepository<CustomerB2B, UUID> {

    @Query("SELECT cb2b from CustomerB2B cb2b WHERE cb2b.customerId.customer.customerId = :customerId")
    Optional<CustomerB2B> findByCustomerId(@Param("customerId") UUID customerId);


    @Query("SELECT cb2b from CustomerB2B cb2b WHERE cb2b.customerId.customer.company.companyId = :companyId")
    List<CustomerB2B> findAllByCompanyId(@Param("companyId") UUID companyId);
}
