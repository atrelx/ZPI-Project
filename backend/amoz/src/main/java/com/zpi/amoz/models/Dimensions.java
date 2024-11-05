package com.zpi.amoz.models;

import com.zpi.amoz.enums.UnitDimensions;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
public class Dimensions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID dimensionsId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitDimensions unitDimensions = UnitDimensions.M;

    private Double height;
    private Double length;
    private Double width;

    @OneToMany(mappedBy = "dimensions", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductVariant> productVariants;

    public UUID getDimensionsId() {
        return dimensionsId;
    }

    public void setDimensionsId(UUID dimensionsId) {
        this.dimensionsId = dimensionsId;
    }

    public UnitDimensions getUnitDimensions() {
        return unitDimensions;
    }

    public void setUnitDimensions(UnitDimensions unitDimensions) {
        this.unitDimensions = unitDimensions;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }
}
