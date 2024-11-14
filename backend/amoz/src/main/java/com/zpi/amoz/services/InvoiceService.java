package com.zpi.amoz.services;

import com.zpi.amoz.models.Invoice;
import com.zpi.amoz.models.ProductOrder;
import com.zpi.amoz.repository.InvoiceRepository;
import com.zpi.amoz.repository.ProductOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {


    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public Invoice generateInvoice(UUID productOrderId) {
        ProductOrder productOrder = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find product order for given id: " + productOrderId));

        if (productOrder.getCustomer() == null) {
            throw new EntityNotFoundException("Customer is required to generate an invoice");
        }

        if (productOrder.getInvoice() != null) {
            invoiceRepository.delete(productOrder.getInvoice());
        }

        Invoice invoice = this.createInvoice(productOrder);
        productOrder.setInvoice(invoice);
        invoiceRepository.flush();
        entityManager.refresh(invoice);
        return invoice;
    }

    private Invoice createInvoice(ProductOrder productOrder) {
        Invoice invoice = new Invoice();
        invoice.setAmountOnInvoice(productOrder.getTotalDue());
        return invoiceRepository.save(invoice);
    }
}

