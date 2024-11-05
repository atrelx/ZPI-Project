package com.zpi.amoz.services;

import com.zpi.amoz.models.Invoice;
import com.zpi.amoz.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> findById(UUID id) {
        return invoiceRepository.findById(id);
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public boolean deleteById(UUID id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

