package com.zpi.amoz.services;

import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import com.zpi.amoz.requests.ProductVariantCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductVariantService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private VariantAttributeRepository variantAttributeRepository;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private DimensionsRepository dimensionsRepository;

    public List<ProductVariant> findAllByProductId(UUID productId) {
        return productVariantRepository.findAllByProductId(productId).stream().filter(ProductVariant::isActive).collect(Collectors.toList());
    }
    public Optional<ProductVariant> findById(UUID id) {
        return productVariantRepository.findById(id);
    }

    public ProductVariant save(ProductVariant productVariant) {
        return productVariantRepository.save(productVariant);
    }

    @Transactional
    public ProductVariant createProductVariant(ProductVariantCreateRequest request) {
        Product product = productRepository.findById(request.productID())
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find product for given ID: " + request.productID()));

        ProductVariant initialProductVariant = new ProductVariant();

        Stock initialStock = new Stock();
        initialStock.setAmountAvailable(request.stock().amountAvailable());
        initialStock.setAlarmingAmount(request.stock().alarmingAmount().orElse(null));
        Stock stock = stockRepository.save(initialStock);
        initialProductVariant.setStock(stock);

        if (request.weight().isPresent()) {
            Weight initialWeight = new Weight();
            initialWeight.setUnitWeight(request.weight().get().unitWeight());
            initialWeight.setAmount(request.weight().get().amount());
            Weight weight = weightRepository.save(initialWeight);
            initialProductVariant.setWeight(weight);
        }

        if (request.dimensions().isPresent()) {
            Dimensions initialDimensions = new Dimensions();
            initialDimensions.setUnitDimensions(request.dimensions().get().unitDimensions());
            initialDimensions.setHeight(request.dimensions().get().height());
            initialDimensions.setWidth(request.dimensions().get().width());
            initialDimensions.setLength(request.dimensions().get().length());
            Dimensions dimensions = dimensionsRepository.save(initialDimensions);
            initialProductVariant.setDimensions(dimensions);
        }

        initialProductVariant.setProduct(product);
        initialProductVariant.setVariantName(request.variantName().orElse(null));

        initialProductVariant.setCode(request.productVariantCode());
        initialProductVariant.setVariantPrice(request.variantPrice());

        ProductVariant productVariant = productVariantRepository.save(initialProductVariant);

        List<VariantAttribute> variantAttributes = request.variantAttributes().stream()
                .map(attribute -> attributeService.createVariantAttribute(attribute, productVariant.getProductVariantId()))
                .collect(Collectors.toList());

        productVariant.setVariantAttributes(variantAttributes);

        return productVariantRepository.save(productVariant);
    }

    @Transactional
    public ProductVariant updateProductVariant(UUID productVariantId, ProductVariantCreateRequest request) {
        ProductVariant existingProductVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find product variant for given ID: " + productVariantId));

        Product product = productRepository.findById(request.productID())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find product for given ID: " + request.productID()));

        Stock stock = existingProductVariant.getStock();
        if (stock == null) {
            stock = new Stock();
        }
        stock.setAmountAvailable(request.stock().amountAvailable());
        stock.setAlarmingAmount(request.stock().alarmingAmount().orElse(null));
        existingProductVariant.setStock(stockRepository.save(stock));

        if (request.weight().isPresent()) {
            Weight weight = existingProductVariant.getWeight();
            if (weight == null) {
                weight = new Weight();
            }
            weight.setUnitWeight(request.weight().get().unitWeight());
            weight.setAmount(request.weight().get().amount());
            existingProductVariant.setWeight(weightRepository.save(weight));
        } else {
            existingProductVariant.setWeight(null);
        }

        if (request.dimensions().isPresent()) {
            Dimensions dimensions = existingProductVariant.getDimensions();
            if (dimensions == null) {
                dimensions = new Dimensions();
            }
            dimensions.setUnitDimensions(request.dimensions().get().unitDimensions());
            dimensions.setHeight(request.dimensions().get().height());
            dimensions.setWidth(request.dimensions().get().width());
            dimensions.setLength(request.dimensions().get().length());
            existingProductVariant.setDimensions(dimensionsRepository.save(dimensions));
        } else {
            existingProductVariant.setDimensions(null);
        }

        existingProductVariant.setProduct(product);
        existingProductVariant.setVariantName(request.variantName().orElse(null));
        existingProductVariant.setCode(request.productVariantCode());
        existingProductVariant.setVariantPrice(request.variantPrice());

        variantAttributeRepository.deleteAllByProductVariantId(productVariantId);

        List<VariantAttribute> newAttributes = request.variantAttributes().stream()
                .map(attribute -> attributeService.createVariantAttribute(attribute, productVariantId))
                .collect(Collectors.toList());
        existingProductVariant.setVariantAttributes(newAttributes);

        return productVariantRepository.save(existingProductVariant);
    }

    @Transactional
    public void deactivateProductVariantById(UUID productVariantId) throws EntityNotFoundException {
        productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find product for given id: " + productVariantId));

        productVariantRepository.deactivateProductVariant(productVariantId);
    }

    public String getFullProductName(ProductVariant productVariant) {
        return  productVariant.getProduct().getName()
                + (productVariant.getVariantName() != null ? ", " + productVariant.getVariantName() : "");
    }

}
