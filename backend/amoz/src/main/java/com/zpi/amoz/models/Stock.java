package com.zpi.amoz.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID stockId;

    @Column(nullable = false)
    private Integer amountAvailable;

    @Column
    private Integer alarmingAmount;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductVariant> productVariants;

    public UUID getStockId() {
        return stockId;
    }

    public void setStockId(UUID stockId) {
        this.stockId = stockId;
    }

    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Integer amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public void increaseStock(int amount) {
        if (amount > 0) {
            this.amountAvailable += amount;
        }
    }

    public void decreaseStock(int amount) {
        if (amount > 0 && this.amountAvailable >= amount) {
            this.amountAvailable -= amount;
        }
    }

    public Integer getAlarmingAmount() {
        return alarmingAmount;
    }

    public void setAlarmingAmount(Integer alarmingAmount) {
        this.alarmingAmount = alarmingAmount;
    }

    public boolean isAlarmTriggered() {
        if (alarmingAmount != null) {
            return amountAvailable <= alarmingAmount;
        }
        return false;
    }

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }
}
