package com.zpi.amoz.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Weight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID weightId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitWeight unitWeight = UnitWeight.KG;

    @Column(nullable = false)
    private Integer amount;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }

    public enum UnitWeight {
        MG, G, KG
    }
}
