package com.zpi.amoz.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class ProductOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productOrderItemId;

    @ManyToOne
    @JoinColumn(name = "productVariantId", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "productOrderId", nullable = false)
    private ProductOrder productOrder;

    @Column(nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private int amount;

    @Column(length = 100)
    private String productName;

    public UUID getProductOrderItemId() {
        return productOrderItemId;
    }

    public void setProductOrderItemId(UUID productOrderItemId) {
        this.productOrderItemId = productOrderItemId;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
