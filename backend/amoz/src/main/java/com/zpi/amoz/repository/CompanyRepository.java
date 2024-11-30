package com.zpi.amoz.repository;

import com.zpi.amoz.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query("SELECT e.company FROM Employee e WHERE e.user.userId = :userId")
    Optional<Company> getCompanyByUserId(String userId);

    @Query("SELECT e.company FROM Employee e WHERE e.employeeId = :employeeId")
    Optional<Company> getCompanyByEmployeeId(UUID employeeId);

    @Query("SELECT cu.company FROM Customer cu WHERE cu.customerId = :customerId")
    Optional<Company> getCompanyByCustomerId(UUID customerId);
}

