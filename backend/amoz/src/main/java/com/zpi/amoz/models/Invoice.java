package com.zpi.amoz.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID invoiceId;

    @Column(nullable = false, unique = true)
    private int invoiceNumber;

    @Column(nullable = false)
    private double amountOnInvoice;

    @OneToOne
    @JoinColumn(name = "ProductOrderID", nullable = false)
    private ProductOrder productOrder;

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public double getAmountOnInvoice() {
        return amountOnInvoice;
    }

    public void setAmountOnInvoice(double amountOnInvoice) {
        this.amountOnInvoice = amountOnInvoice;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }
}
