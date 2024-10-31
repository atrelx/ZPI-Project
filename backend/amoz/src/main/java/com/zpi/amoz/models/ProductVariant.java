package com.zpi.amoz.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productVariantId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false, unique = true)
    private Integer code;

    @ManyToOne
    @JoinColumn(name = "stockID", nullable = false)
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "dimensionsID")
    private Dimensions dimensions;

    @ManyToOne
    @JoinColumn(name = "weightID")
    private Weight weight;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;
    private Double impactOnPrice;

    @Column(length = 100)
    private String variantName;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VariantAttribute> variantAttributes;

    public UUID getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(UUID productVariantId) {
        this.productVariantId = productVariantId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
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

    public Double getImpactOnPrice() {
        return impactOnPrice;
    }

    public void setImpactOnPrice(Double impactOnPrice) {
        this.impactOnPrice = impactOnPrice;
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
}
