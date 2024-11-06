package com.zpi.amoz.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ProductVariantID", "AttributeID"})
})
public class VariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID variantAttributeId;

    @ManyToOne
    @JoinColumn(name = "productVariantId", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "attributeId", nullable = false)
    private Attribute attribute;

    @Column(length = 255)
    private String value;

    public UUID getVariantAttributeId() {
        return variantAttributeId;
    }

    public void setVariantAttributeId(UUID variantAttributeId) {
        this.variantAttributeId = variantAttributeId;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

