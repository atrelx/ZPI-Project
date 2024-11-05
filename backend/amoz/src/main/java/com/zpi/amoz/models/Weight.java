package com.zpi.amoz.models;

import com.zpi.amoz.enums.UnitWeight;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
public class Weight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID weightId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitWeight unitWeight = UnitWeight.KG;

    @Min(0)
    @Column(nullable = false)
    private Double amount;

    @OneToMany(mappedBy = "weight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductVariant> productVariants;

    public UUID getWeightId() {
        return weightId;
    }

    public void setWeightId(UUID weightId) {
        this.weightId = weightId;
    }

    public UnitWeight getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(UnitWeight unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }
}
