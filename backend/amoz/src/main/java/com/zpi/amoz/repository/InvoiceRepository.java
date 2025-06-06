package com.zpi.amoz.repository;


import com.zpi.amoz.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

}

