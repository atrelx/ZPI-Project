package com.zpi.amoz.services;

import com.sun.source.doctree.AttributeTree;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import com.zpi.amoz.requests.AttributeCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired

    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private VariantAttributeRepository variantAttributeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;


    public List<Attribute> findAll() {
        return attributeRepository.findAll();
    }

    public Optional<Attribute> findById(UUID id) {
        return attributeRepository.findById(id);
    }

    public Attribute save(Attribute attribute) {
        return attributeRepository.save(attribute);
    }

    public boolean deleteById(UUID id) {
        if (attributeRepository.existsById(id)) {
            attributeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Attribute> getAllProductAttributes(UUID companyId) {
        return attributeRepository.fetchAllProductAttributesByCompanyId(companyId);
    }

    public List<Attribute> getAllVariantAttributes(UUID companyId) {
        return attributeRepository.fetchAllVariantAttributesByCompanyId(companyId);
    }

    public List<Attribute> getAllAttributes(UUID companyId) {
        return attributeRepository.fetchAllAttributesByCompanyId(companyId.toString());
    }

    @Transactional
    public ProductAttribute createProductAttribute(AttributeCreateRequest request, UUID productId) {
        Attribute attribute = createAttribute(request);

        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setValue(request.value().orElse(null));
        productAttribute.setAttribute(attribute);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find product for given id: " + productId));
        productAttribute.setProduct(product);

        return productAttributeRepository.save(productAttribute);
    }

    @Transactional
    public VariantAttribute createVariantAttribute(AttributeCreateRequest request, UUID productVariantId) {
        Attribute attribute = createAttribute(request);

        VariantAttribute variantAttribute = new VariantAttribute();
        variantAttribute.setValue(request.value().orElse(null));
        variantAttribute.setAttribute(attribute);

        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find product variant for given id: " + productVariantId));
        variantAttribute.setProductVariant(productVariant);

        return variantAttributeRepository.save(variantAttribute);
    }

    private Attribute createAttribute(AttributeCreateRequest request) {
        if (attributeRepository.findAttributeByName(request.attributeName()).isPresent()) {
            return attributeRepository.findAttributeByName(request.attributeName()).get();
        } else {
            Attribute attribute = new Attribute();
            attribute.setAttributeName(request.attributeName());
            return attributeRepository.save(attribute);
        }
    }
}

