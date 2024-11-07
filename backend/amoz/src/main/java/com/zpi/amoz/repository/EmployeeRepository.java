package com.zpi.amoz.repository;


import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByUser_UserId(String userId);
    List<Employee> findAllByCompany(Company company);
}

