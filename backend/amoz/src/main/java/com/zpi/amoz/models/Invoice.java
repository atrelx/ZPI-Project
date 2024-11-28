package com.zpi.amoz.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID invoiceId;

    @Generated(GenerationTime.INSERT)
    @Column(nullable = false, unique = true, insertable = false)
    private int invoiceNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountOnInvoice;

    @Column(nullable = false)
    private LocalDate issueDate = LocalDate.now();

    @OneToOne(mappedBy = "invoice", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ProductOrder productOrder;

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public BigDecimal getAmountOnInvoice() {
        return amountOnInvoice;
    }

    public void setAmountOnInvoice(BigDecimal amountOnInvoice) {
        this.amountOnInvoice = amountOnInvoice;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }
    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
