package com.zpi.amoz.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID productVariantId;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true)
    private Integer code;

    @ManyToOne
    @JoinColumn(name = "stockId", nullable = false)
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "dimensionsId")
    private Dimensions dimensions;

    @ManyToOne
    @JoinColumn(name = "weightId")
    private Weight weight;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal variantPrice;

    @Column(length = 100)
    private String variantName;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VariantAttribute> variantAttributes;

    @Column(nullable = false)
    private boolean isActive = true;

    public UUID getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(UUID productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public BigDecimal getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(BigDecimal variantPrice) {
        this.variantPrice = variantPrice;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<VariantAttribute> getVariantAttributes() {
        return variantAttributes;
    }

    public void setVariantAttributes(List<VariantAttribute> variantAttributes) {
        this.variantAttributes = variantAttributes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
