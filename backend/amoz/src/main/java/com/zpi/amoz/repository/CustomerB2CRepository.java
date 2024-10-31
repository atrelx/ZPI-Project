package com.zpi.amoz.repository;


import com.zpi.amoz.models.CustomerB2C;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerB2CRepository extends JpaRepository<CustomerB2C, UUID> {
}

