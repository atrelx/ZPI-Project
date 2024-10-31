package com.zpi.amoz.repository;

import com.zpi.amoz.models.CustomerB2B;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerB2BRepository extends JpaRepository<CustomerB2B, UUID> {
}
